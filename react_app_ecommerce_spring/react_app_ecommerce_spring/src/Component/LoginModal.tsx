import { useState, type FormEvent } from "react"
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { login } from "../redux/authSlice";



export default function LoginModal({ onClose }: { onClose: () => void }) {
  
  const navigate = useNavigate();
  const dispatch = useDispatch();

  
  const [messageLocation, setMessageLocation] = useState("");
  const [formData, setFormData ] = useState({
    email: "",
    password: ""
  })

  const handleChange = async (e: FormEvent<HTMLInputElement>) =>{
    setFormData({...formData, [e.currentTarget.name]: e.currentTarget.value});
  }
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();



    
    //creo oggetto che mi crea una stringa tipo key1=value1&key2=value2 usata nel body della richiesta
    const form = new URLSearchParams();
    //al submit, ad ogni coppia (chiave, valore) raccolta dal handleChange mi fa un append su 'form'
    Object.entries(formData).forEach(([key, value]) => form.append(key, value));

    try{
      console.log("SONO NEL TRY");
      const response = await fetch("http://localhost:8095/login",({
        method: "POST",
        headers: {
        "Content-Type": "application/x-www-form-urlencoded"
        },
        body: form,
        credentials: "include"
      }))

      //dalla risposta del fetch mi prendo il content tyoe
      const contentType = response.headers.get("Content-Type");

      //controllo se il content type è application json
      const body = contentType?.includes("application/json") ? await response.json() : await response.text();

      //se la richiesta non va a buon fine
      if(!response.ok && response.status != 400 ){
        
        //stampo l'errore e setto il messaggio che mi viene gestito in <Home >
        console.error("Status Text: \n" + body.message + "\n" + response.statusText)
        setMessageLocation("Errore. La richiesta non è andata a buon fine.")
      }
      
      //per far partire l'authSlice che setta lo state.isLoggedNow = true 
      //(il valore lo gestisco poi in Home.tsx per i button)
      dispatch(login());
      console.log("Login riuscito");

      //chiudo il modale, altrimenti non funziona il redirect (nel navigate dopo)
      onClose();

       //Per far partire il redirect alla Home (seguito da un messaggio)
      setTimeout(()=> navigate("/", { state: { messageLocation: "Accesso avvenuto con successo" } }), 500)
      


    }catch(err){
      setMessageLocation("Errore. Abbiamo riscontrato un errore nella richiesta");
      console.log("ERRORE NELLA RICHIESTA" + err);
    }

    console.log(messageLocation);

  }

  
  return (
    <div className="fixed inset-0  flex justify-center items-center z-50">
      <div className="bg-black opacity-20 w-[100vw] h-[100%] absolute">

      </div>
      <div className="bg-white p-6 rounded-lg shadow-lg w-[400px] relative">
        <button
          onClick={onClose}
          className="absolute top-2 right-2 text-gray-500 hover:text-gray-800"
        >
          ✕
        </button>
        <h2 className="text-2xl font-semibold mb-4">Login</h2>
        {/* Qui puoi usare un form con email/password */}
        <form className="flex flex-col gap-3" onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Email"
            className="border p-2 rounded"
            name="email"
            value={formData.email}
            onChange={handleChange}
          />
          <input
            type="password"
            placeholder="Password"
            className="border p-2 rounded"
            name="password"
            value={formData.password}
            onChange={handleChange}
          />
          <button type="submit" className="bg-blue-600 text-white py-2 rounded mt-2" >
            Accedi
          </button>
        </form>
      </div>
    </div>
  );
};

