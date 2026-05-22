import { Routes, Route, Navigate } from 'react-router-dom'
import ProtectedRoute from './components/ProtectedRoute'
import Register from './pages/Register'
import Login from './pages/Login'
import Home from './pages/Home'

export default function App() {
  return (
    <Routes>
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/home" element={
         <ProtectedRoute>
             <Home />
         </ProtectedRoute>
       } />
      <Route path="/" element={<Navigate to="/register" replace />} />
    </Routes>
  )
}