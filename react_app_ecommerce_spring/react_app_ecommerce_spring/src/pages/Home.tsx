import useProducts from "../hooks/useProducts";
import type { RootState } from '../redux/store';
import { useSelector } from "react-redux";
import CardItem from '../component/CardItem'
import { Store, Filter } from 'lucide-react';
import Navbar from "../component/Navbar";
import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";



type Message = {
  message: string;
}

// Home.tsx
function Home({ message }: Message) {
  useProducts();
  const products = useSelector((state: RootState) => state.products.value);
  const [showPopup, setShowPopup] = useState(false);

  //mi prendo messageLocation dallo stato dalla pagina corrente
  const location = useLocation();
  const messageLocation = location.state?.messageLocation || "";

  //message mi arriva tramite <Home message="..."
  //messageLocation mi arriva qui dal navigate in LoginModal
  const finalMessage= message || messageLocation;


  useEffect(() => {
    if (finalMessage !== "") {
      const delay = setTimeout(() => setShowPopup(true), 1500);      // mostra dopo 1s
      const hide = setTimeout(() => {
        setShowPopup(false);
        window.history.replaceState({}, document.title);

      }, 5000);

      return () => {
        clearTimeout(delay);
        clearTimeout(hide);
      };
    }
  }, [finalMessage]);

  return (
    <>
      
      <Navbar></Navbar>
      {
        finalMessage.includes("creato") || finalMessage.includes("avvenuto") ?
          <div className={`
          transition-all duration-500 ease-out
          ${showPopup ? "opacity-100 scale-100" : "opacity-0 scale-95 pointer-events-none"}
          mt-6 mx-auto max-w-md text-center text-green-600 font-medium 
          bg-green-100 border border-green-400 px-6 py-4 rounded-xl shadow-md
        `}>
            {finalMessage}
          </div>
          :
          <div className={`
          transition-all duration-500 ease-out
          ${showPopup ? "opacity-100 scale-100" : "opacity-0 scale-95 pointer-events-none"}
          mt-6 mx-auto max-w-md text-center text-red-600 font-medium 
          bg-red-100 border border-red-400 px-6 py-4 rounded-xl shadow-md
        `}>{finalMessage}</div>
        }

        

      
      <div className="min-h-screen bg-gray-50">
        <div className="w-full mx-auto px-4 py-8">
          {/* Header */}
          <div className="mb-8">
            <div className="flex items-center gap-3 mb-4">
              <Store className="text-indigo-600" size={32} />
              <h1 className="text-4xl font-bold text-gray-900">Prodotti disponibili</h1>
            </div>
            <p className="text-gray-600 text-lg">Scopri i nostri prodotti</p>
          </div>

          {/* Filter Bar */}
          <div className="mb-8 bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Filter size={18} className="text-gray-500" />
                <span className="text-gray-700 font-medium">Prodotti totali: {products.length}</span>
              </div>
              <div className="flex gap-3">
                <select className="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                  <option>Tutte le categorie</option>
                  <option>Elettronica</option>
                  <option>Audio</option>
                  <option>Gaming</option>
                </select>
                <select className="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500">
                  <option>Ordina per prezzo</option>
                  <option>Prezzo: crescente</option>
                  <option>Prezzo: decrescente</option>
                  <option>Nome A-Z</option>
                </select>
              </div>
            </div>
          </div>

          {/* Product Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 p-4">
            {products.map((prodotto) => (
              <CardItem
                key={prodotto.id}
                nomeProdotto={prodotto.nomeProdotto}
                descrizione={prodotto.descrizione}
                marca={prodotto.marca}
                prezzo={prodotto.prezzo}
                categoria={prodotto.categoria}
                base64image={prodotto.base64image}
                quantita={prodotto.quantita}
              ></CardItem>
            ))}
          </div>

          {/* Load More Button */}
          <div className="mt-12 text-center">
            <button className="px-8 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 shadow-md hover:shadow-lg">
              Carica più prodotti
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default Home;