import { useEffect } from 'react';
import { useNavigate, Outlet } from 'react-router-dom';

import { LogOut } from 'lucide-react';

import { toast } from '@/hooks/use-toast';
import { useNotifications } from '@/hooks/useNotifications';

import { Button } from '@/components/ui/button';
import { AppSidebar } from '@/components/AppSidebar';
import { NotificationDropdown } from '@/components/NotificationDropdown';
import { SidebarProvider, SidebarTrigger } from '@/components/ui/sidebar';

const StaffLayout = () => {
  const navigate = useNavigate();
  
  const {
    notifications,
    unreadCount,
    addNotification,
    markAsRead,
    markAllAsRead,
    deleteNotification,
    clearAll,
  } = useNotifications();

  useEffect(() => {
    const isAuth = localStorage.getItem('staffAuth');
    if(!isAuth) navigate('/staff/login');
  }, [navigate]);

  // Simulate new order notifications - In real app, this would be from websocket/polling
  useEffect(() => {
    const interval = setInterval(() => {
      // Simulate random new orders for demo purposes
      if(Math.random() > 0.95) {
        const orderCode = `#${Math.floor(Math.random() * 9000) + 1000}`;
        addNotification(
          'new_order',
          'Novo Pedido Recebido',
          `Pedido ${orderCode} foi registrado no sistema`,
          orderCode
        );
        toast({
          title: '🔔 Novo Pedido!',
          description: `Pedido ${orderCode} foi recebido`,
        });
      }
    }, 30000); // Check every 30 seconds

    return () => clearInterval(interval);
  }, [addNotification]);

  const handleLogout = () => {
    localStorage.removeItem('staffAuth');
    navigate('/staff/login');
  };

  return (
    <SidebarProvider>
      <div className="min-h-screen flex w-full">
        <AppSidebar />
        <div className="flex-1 flex flex-col">
          <header className="border-b bg-card sticky top-0 z-10">
            <div className="flex items-center justify-between px-4 py-4">
              <div className="flex items-center gap-2">
                <SidebarTrigger />
              </div>
              <div className="flex items-center gap-2">
                <NotificationDropdown
                  onClearAll={clearAll}
                  unreadCount={unreadCount}
                  onMarkAsRead={markAsRead}
                  notifications={notifications}
                  onDelete={deleteNotification}
                  onMarkAllAsRead={markAllAsRead}
                />
                <Button variant="outline" onClick={handleLogout}>
                  <LogOut className="h-4 w-4 mr-2" />Sair</Button>
              </div>
            </div>
          </header>
          <main className="flex-1 p-6">
            <Outlet />
          </main>
        </div>
      </div>
    </SidebarProvider>
  );
};

export default StaffLayout;