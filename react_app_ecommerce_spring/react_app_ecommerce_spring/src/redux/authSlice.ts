import { createSlice } from '@reduxjs/toolkit'
const initialState = {
    isLoggedNow:false
    
}

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        login: (state) => {
            state.isLoggedNow = true;
        },
        logout: (state) => {
            state.isLoggedNow = false;
        },
    }
})

export const { login, logout} = authSlice.actions;
export const authReducer = authSlice.reducer;