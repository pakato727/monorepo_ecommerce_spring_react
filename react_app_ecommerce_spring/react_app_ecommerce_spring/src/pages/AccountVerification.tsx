import React, { useEffect, useRef, useState } from "react";
import { useSearchParams } from "react-router-dom";
import Home from "./Home";


const AccountVerification = () => {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState<"" | "loading" | "verified" | "success" | "expired" | "error" | "failure" | "empty" | "invalid">("loading");


  const redirectRef = useRef<HTMLAnchorElement>(null);


  useEffect(() => {
    const token = searchParams.get("token");
    if (!token) {
      console.log(token);
      setStatus("invalid");
      return;
    }
    const redirectUrl = "/server/accountVerify(token=${token})}";

    const timer = setTimeout(() => {
      fetch(`http://localhost:8095/api/accountVerify?token=${token}`,
        {
          method: "GET",
          credentials: "include",
        })
        .then(async (response) => {
          const data = await response.json();
          console.log("Verifica effettuata: " + data)
          setStatus("success");
          //se la richiesta è andata a buon fine (vedendo se arriva "successo":"verificato" dal back end)
          if (data.successo === "verificato") {

            //imposta un tempo di reindirizzamento settato a 2 secondi 
            //se non lo imposto l'ordinamento dei processi non arriva in modo corretto
            //e quindi il risultato non sarà quello sperato
            const timerRedirect = setTimeout(() => {
              if (redirectRef.current) {
                window.location.href = redirectUrl;

              }
            }, 2000)
            return () => clearTimeout(timerRedirect)
          } else {
            setStatus("failure");
          }
        })
        .catch(async (error) => {
          if (error.response?.data?.successo === "expired") {
            console.log("Scaduto")
            setStatus("expired");
          } else if (error.response?.data?.successo === "vuoto") {
            console.log("Vuoto")
            setStatus("empty");
          } else {
            console.log("errore")
            setStatus("error");
          }
        });
    }, 1000);

    return () => clearTimeout(timer);
  }, [searchParams]);

  const [showPopup, setShowPopup] = useState(false);


  useEffect(() => {
    if (status !== "") {
      const delay = setTimeout(() => setShowPopup(true), 1500);      // mostra dopo 1s
      const hide = setTimeout(() => {
        setShowPopup(false);
      }, 5000);

      return () => {
        clearTimeout(delay);
        clearTimeout(hide);
      };
    }
  }, [status]);

  return (
    <>
      <Home message=""></Home>

      <div className={`transition-all duration-500 ease-out
          ${showPopup ? "opacity-100 scale-100" : "opacity-0 scale-95 pointer-events-none"}
          mt-6 mx-auto max-w-md text-center text-black font-medium 
          bg-white border border-black px-6 py-4 rounded-xl shadow-md absolute left-[50%] top-[20%] transform: translate-[-50%]`} style={{ textAlign: "center", padding: "2rem", fontFamily: "Arial, sans-serif" }}>
        {status === "loading" && <p>Verifica in corso...</p>}
        {status === "success" && <p>✅ La verifica è terminata, l'account è stato confermato con successo!</p>}
        {status === "expired" && <p>⚠️ Il token è scaduto. Registrati di nuovo.</p>}
        {status === "invalid" && <p>❌ Token non valido.</p>}
        {status === "empty" && <p>❌ Token mancante.</p>}
        {status === "error" && <p>❌ Si è verificato un errore. </p>}
        {status === "failure" && <p>❌ L'account non è stato verificato correttamente, ci scusiamo per il disagio. </p>}

      </div>
    </>
  );
};

export default AccountVerification;