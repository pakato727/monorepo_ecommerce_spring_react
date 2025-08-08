import { configureStore } from '@reduxjs/toolkit'
import {productsReducer} from './productsSlice'
import { authReducer } from './authSlice';
import { roleReducer } from './roleSlice';

export const store = configureStore({
    reducer:{
        products: productsReducer,
        auth: authReducer,
        role: roleReducer
    },
})

export type RootState = ReturnType<typeof store.getState>;