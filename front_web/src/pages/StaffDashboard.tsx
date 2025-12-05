import { useState } from 'react';
import { mockOrders } from '@/lib/mockData';
import { Order, OrderStatus } from '@/types/order';
import { OrderCard } from '@/components/staff/OrderCard';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import {
  Dialog,
  DialogTitle,
  DialogHeader,
  DialogContent,
} from '@/components/ui/dialog';

const StaffDashboard = () => {
  const [orders, setOrders] = useState<Order[]>(mockOrders);
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);

  const handleStatusChange = (orderId: string, newStatus: OrderStatus) => {
    setOrders((prev) =>
      prev.map((order) =>
        order.id === orderId ? { ...order, status: newStatus } : order
      )
    );
  };

  const getOrdersByStatus = (status: OrderStatus) => {
    return orders.filter((order) => order.status === status);
  };

  const statusTabs = [
    { value: 'all',       label: 'All Orders', count: orders.length },
    { value: 'received',  label: 'Received',   count: getOrdersByStatus('received').length },
    { value: 'preparing', label: 'Preparing',  count: getOrdersByStatus('preparing').length },
    { value: 'ready',     label: 'Ready',      count: getOrdersByStatus('ready').length },
  ];

  return (
    <div className="space-y-6">
        <Tabs defaultValue="all" className="space-y-6">
          <TabsList className="grid w-full max-w-2xl grid-cols-4">
            {statusTabs.map((tab) => (
              <TabsTrigger key={tab.value} value={tab.value} className="relative">
                {tab.label}
                {tab.count > 0 && (
                  <span className="ml-2 bg-primary/20 text-primary px-2 py-0.5 rounded-full text-xs font-bold">
                    {tab.count}
                  </span>
                )}
              </TabsTrigger>
            ))}
          </TabsList>

          <TabsContent value="all" className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
              {orders.map((order) => (
                <OrderCard
                  key={order.id}
                  order={order}
                  onStatusChange={handleStatusChange}
                  onViewDetails={setSelectedOrder}
                />
              ))}
            </div>
          </TabsContent>

          {(['received', 'preparing', 'ready'] as OrderStatus[]).map((status) => (
            <TabsContent key={status} value={status} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                {getOrdersByStatus(status).map((order) => (
                  <OrderCard
                    key={order.id}
                    order={order}
                    onStatusChange={handleStatusChange}
                    onViewDetails={setSelectedOrder}
                  />
                ))}
              </div>
              {getOrdersByStatus(status).length === 0 && (
                <div className="text-center py-12">
                  <p className="text-muted-foreground">No {status} orders</p>
                </div>
              )}
            </TabsContent>
          ))}
        </Tabs>

      {/* Order Details Dialog */}
      <Dialog open={!!selectedOrder} onOpenChange={() => setSelectedOrder(null)}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Order Details - {selectedOrder?.orderCode}</DialogTitle>
          </DialogHeader>
          {selectedOrder && (
            <div className="space-y-4">
              <div>
                <h4 className="font-semibold mb-2">Student Information</h4>
                <div className="text-sm space-y-1">
                  <p><span className="text-muted-foreground">Name:</span> {selectedOrder.student.name}</p>
                  <p><span className="text-muted-foreground">Class:</span> {selectedOrder.student.studentClass}</p>
                </div>
              </div>
              <div>
                <h4 className="font-semibold mb-2">Items</h4>
                <div className="space-y-2">
                  {selectedOrder.items.map((item) => (
                    <div key={item.id} className="flex justify-between text-sm">
                      <span>{item.quantity}x {item.name}</span>
                      <span>${(item.price * item.quantity).toFixed(2)}</span>
                    </div>
                  ))}
                  <div className="pt-2 border-t flex justify-between font-bold">
                    <span>Total</span>
                    <span className="text-primary">${selectedOrder.total.toFixed(2)}</span>
                  </div>
                </div>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default StaffDashboard;
