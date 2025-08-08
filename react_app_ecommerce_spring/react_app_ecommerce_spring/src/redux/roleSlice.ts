import { createSlice } from "@reduxjs/toolkit";


const initialState = {
    role: "",
}

const roleSlice = createSlice({
    name:"role",
    initialState,
    reducers: {
        roleSuper: (state) => {
            state.role = "SADMIN";
        },
        roleAdmin: (state) => {
            state.role = "ADMIN";
        },
        roleGuest: (state) => {
            state.role = "GUEST";
        },
        roleBanned: (state) => {
            state.role = "BANNED"
        },
    }
})

export const {roleAdmin, roleBanned, roleGuest, roleSuper} =  roleSlice.actions;
export const roleReducer = roleSlice.reducer;