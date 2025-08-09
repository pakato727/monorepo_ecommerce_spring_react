import { createSlice } from "@reduxjs/toolkit";


const initialState = {
    role: "",
    roles: [],
}

const roleSlice = createSlice({
    name:"roles",
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
        setRole: (state, action)=> {
            state.roles = action.payload;
        },
        clearRole: (state) => {
            state.roles = [];
        }
    }
})

// roleAdmin, roleBanned, roleGuest, roleSuper
export const {roleAdmin, roleBanned, roleGuest, roleSuper, setRole, clearRole} =  roleSlice.actions;
export const roleReducer = roleSlice.reducer;