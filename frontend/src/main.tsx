import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom'
import { AuthProvider } from './components/context/AuthProvider'
import { RequireAuth } from './components/context/RequireAuth'
import HomePage from './Pages/HomePage'
import NotFoundPage from './Pages/NotFoundPage'

const router = createBrowserRouter(
  createRoutesFromElements(
    <>
      <Route element={<RequireAuth />}>
        <Route path="/" element={<HomePage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </>
  )
)

createRoot(document.getElementById('root')!).render(
  <AuthProvider>
    <StrictMode>
      <RouterProvider router={router} />
    </StrictMode>
  </AuthProvider>
)
