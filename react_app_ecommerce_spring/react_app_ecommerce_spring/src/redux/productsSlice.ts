import { createSlice } from '@reduxjs/toolkit'
const initialState = {
    value: [{
        id: 888,
        nomeProdotto: "prodottoState",
        base64image: "basestringa64State",
        descrizione: "descrizioneState",
        prezzo: 44.9,
        quantita: 1,
        categoria: "categoriaState",
        marca: "marcaState",
    }]
}

const productsSlice = createSlice({
    name: "products",
    initialState,
    reducers: {
        add: (state, action) => {
            //penso dovrebbe andare il fetch dell aggiunta al db in backend
            //e in base alla risposta:
            state.value.push(action.payload)
        },
        reset: () => initialState,
        setAll: (state, action) => {
            state.value = action.payload;
        }
    }
})

export const { add, reset, setAll } = productsSlice.actions
export const productsReducer = productsSlice.reducer