import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom'
import { AuthProvider } from './components/context/AuthProvider'
import { RequireAuth } from './components/context/RequireAuth'
import HomePage from './Pages/HomePage'
import NotFoundPage from './Pages/NotFoundPage'
import LoginPage from './Pages/LoginPage'
import SignupPage from './Pages/SignupPage'
import { SidebarProvider } from './components/ui/sidebar'
import { TooltipProvider } from './components/ui/tooltip'
import { ThemeProvider } from './components/theme/ThemeProvider'
import DirectMessagesPage from './Pages/DirectMessagesPage/DirectMessagesPage'

const router = createBrowserRouter(
  createRoutesFromElements(
    <>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route element={<RequireAuth />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/direct-messages" element={<DirectMessagesPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </>
  )
)

createRoot(document.getElementById('root')!).render(
  <ThemeProvider defaultTheme="light" storageKey="vite-ui-theme">
    <SidebarProvider>
      <TooltipProvider>
        <AuthProvider>
          <StrictMode>
            <RouterProvider router={router} />
          </StrictMode>
        </AuthProvider>
      </TooltipProvider>
    </SidebarProvider>
  </ThemeProvider>
)
