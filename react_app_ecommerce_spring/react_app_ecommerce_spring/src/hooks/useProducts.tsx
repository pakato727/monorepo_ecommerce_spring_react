import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import type { RootState } from '../redux/store';
import { add, reset,setAll} from "../redux/productsSlice";

interface Prodotto {
    id: number
    nomeProdotto: string,
    base64image: string,
    descrizione: string,
    prezzo: number,
    quantita: number,
    categoria: string,
    marca: string

}

function useProducts() {
    
    const dispatch = useDispatch()
    useEffect(() => {
        console.log(`CHIAMATA useEffect ${Math.floor(Math.random()*100)}`);
        fetch("http://localhost:8095/api/products")
            .then((res) => res.json())
            .then((json: Prodotto[]) => {
                dispatch(setAll(json))
            })
    }, [])


}

export default useProducts;