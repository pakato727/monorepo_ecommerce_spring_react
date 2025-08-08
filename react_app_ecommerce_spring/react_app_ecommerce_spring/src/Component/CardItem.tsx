import { ShoppingCart, Package } from 'lucide-react';
import type { Prodotto } from '../types/Product';

function CardItem({ nomeProdotto, base64image, descrizione, prezzo, quantita, categoria, marca }: Omit<Prodotto, 'id'>) {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden transition-all duration-300 hover:shadow-xl hover:scale-[1.02] group">
      <div className="relative overflow-hidden">
       <img src={`data:image/jpeg;base64,${base64image}`} className="w-full h-48 object-cover" />
        <div className="absolute top-3 right-3">
          <span className={`px-2 py-1 rounded-full text-xs font-medium ${
            quantita > 0 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
          }`}>
            {quantita > 0 ? 'In Stock' : 'Out of Stock'}
          </span>
        </div>
      </div>
      
      <div className="p-5">
        <div className="mb-3">
          <h3 className="text-lg font-bold text-gray-900 mb-1 line-clamp-1">{nomeProdotto}</h3>
          <p className="text-sm text-gray-600 line-clamp-2 leading-relaxed">{descrizione}</p>
        </div>
        
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-2">
            <span className="text-2xl font-bold text-indigo-600">€{prezzo.toFixed(2)}</span>
          </div>
          <div className="flex items-center gap-1 text-gray-500">
            <Package size={14} />
            <span className="text-sm">{quantita}</span>
          </div>
        </div>
        
        <div className="flex items-center justify-between mb-4">
          <div className="flex gap-2">
            <span className="bg-blue-50 text-blue-700 px-3 py-1 rounded-full text-xs font-medium">
              {marca}
            </span>
            <span className="bg-purple-50 text-purple-700 px-3 py-1 rounded-full text-xs font-medium">
              {categoria}
            </span> 
          </div>
        </div>
        
        <button
          className={`w-full py-2.5 px-4 rounded-lg font-medium text-sm transition-all duration-200 flex items-center justify-center gap-2 ${
            quantita > 0
              ? 'bg-indigo-600 text-white hover:bg-indigo-700 hover:shadow-md'
              : 'bg-gray-100 text-gray-400 cursor-not-allowed'
          }`}
          disabled={quantita === 0}
        >
          <ShoppingCart size={16} />
          {quantita > 0 ? 'Add to Cart' : 'Sold Out'}
        </button>
      </div>
    </div>
  );
}

export default CardItem;