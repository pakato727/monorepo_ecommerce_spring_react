import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'

import { createBrowserRouter,RouterProvider } from 'react-router-dom'
import Home from './pages/Home.tsx'

import { store } from './redux/store.ts'
import { Provider } from 'react-redux'
import RegisterForm from './Component/RegisterForm.tsx'
import AccountVerification from './pages/AccountVerification.tsx'


const router= createBrowserRouter([
  {
    path:'/',
    element: <Home message=""></Home>
  },
  {
    path:'/home',
    element: <Home message=""></Home>
  },
  {
    path: '/register',
    element: <RegisterForm></RegisterForm>
  },
  {
    path: '/accountVerify',
    element: <AccountVerification ></AccountVerification>
  }
])
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
      <RouterProvider router={router}/>
    </Provider>
  </StrictMode>
)

