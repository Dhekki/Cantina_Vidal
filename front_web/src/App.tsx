import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";

import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import NotFound from "./pages/NotFound";
import StaffLogin from "./pages/StaffLogin";
import StaffLayout from "./components/StaffLayout";
import StaffDashboard from "./pages/StaffDashboard";
import InternalOrders from "./pages/InternalOrders";
import ProductsManagement from "./pages/ProductsManagement";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          <Route path="/"            element={<Navigate to="/staff/login" replace />} />
          <Route path="/staff/login" element={<StaffLogin />} />
          <Route   path="/staff"     element={<StaffLayout />}>
            <Route path="dashboard"  element={<StaffDashboard />} />
            <Route path="products"   element={<ProductsManagement />} />
            <Route path="orders/new" element={<InternalOrders />} />
          </Route>
          
          {/* ADD ALL CUSTOM ROUTES ABOVE THE CATCH-ALL "*" ROUTE */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;