import { useState } from 'react';
import { mockOrders } from '@/lib/mockData';
import { Order, OrderStatus } from '@/types/order';
import { OrderCard } from '@/components/staff/OrderCard';
import { Tabs, TabsContent, TabsList } from '@/components/ui/tabs';
import { StatusTabsTrigger } from '@/components/ui/status-tabs-trigger';
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
    { value: 'all'       as const, label: 'Todos os Pedidos', count: orders.length },
    { value: 'received'  as const, label: 'Recebidos',        count: getOrdersByStatus('received').length },
    { value: 'preparing' as const, label: 'Em Preparo',       count: getOrdersByStatus('preparing').length },
    { value: 'ready'     as const, label: 'Feitos',           count: getOrdersByStatus('ready').length },
    { value: 'delivered' as const, label: 'Entregues',        count: getOrdersByStatus('delivered').length},
    { value: 'canceled'  as const, label: 'Cancelados',       count: getOrdersByStatus('canceled').length},
  ];

  return (
    <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-semibold text-neutral-700 mb-2">Pedidos</h1>
          <p className="text-sm text-muted-foreground">
            Lorem ipsum dolor sit amet, consectetur adipiscing elitsed consectetur.
          </p>
        </div>
        <Tabs defaultValue="all" className="space-y-6">
          <TabsList className="grid w-full max-w-6xl h-fit grid-cols-6">
            {statusTabs.map((tab) => (
              <StatusTabsTrigger key={tab.value} value={tab.value} status={tab.value} className="relative">
                {tab.label}
                  <span className={`ml-2 bg-status-${tab.value}-secondary text-status-${tab.value}-primary border border-status-${tab.value}-primary w-6 h-6 flex justify-center items-center rounded-full text-xs font-bold text-center`}>
                    {tab.count}
                  </span>
              </StatusTabsTrigger>
            ))}
          </TabsList>

          <TabsContent value="all" className="space-y-4">
              <div
                className="
                  grid
                  gap-4
                  w-full
                  grid-cols-[repeat(auto-fit,minmax(0,400px))]
                  justify-items-start
                  justify-start
                  items-stretch
                "
              >
              {orders.map((order) => (
                <div key={order.id} className="w-full max-w-[400px]">
                  <OrderCard
                    order={order}
                    onViewDetails={setSelectedOrder}
                    onStatusChange={handleStatusChange}
                  />
                </div>
              ))}
            </div>
          </TabsContent>

          {(['received', 'preparing', 'ready'] as OrderStatus[]).map((status) => (
            <TabsContent key={status} value={status} className="space-y-4">
                <div
                  className="
                    grid
                    gap-4
                    w-full
                    grid-cols-[repeat(auto-fit,minmax(0,400px))]
                    justify-items-start
                    justify-start
                    items-stretch
                  "
                >
                {getOrdersByStatus(status).map((order) => (
                  <div key={order.id} className="w-full max-w-[400px]">
                    <OrderCard
                      order={order}
                      onViewDetails={setSelectedOrder}
                      onStatusChange={handleStatusChange}
                    />
                  </div>
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
            <DialogTitle>Detalhes do Pedido - {selectedOrder?.orderCode}</DialogTitle>
          </DialogHeader>
          {selectedOrder && (
            <div className="space-y-4">
              <div>
                <h4 className="font-semibold mb-2">Cliente</h4>
                <div className="text-sm space-y-1">
                  <p><span className="text-muted-foreground">Nome:</span> {selectedOrder.student.name}</p>
                  <p><span className="text-muted-foreground">Retirada:</span> {selectedOrder.pickUpTime}</p>
                </div>
              </div>
              <div>
                <h4 className="font-semibold mb-2">Itens</h4>
                <div className="space-y-2">
                  {selectedOrder.items.map((item) => (
                    <div key={item.id} className="flex justify-between text-sm">
                      <span>{item.quantity}x {item.name}</span>
                      <span>R${(item.price * item.quantity).toFixed(2)}</span>
                    </div>
                  ))}
                  <div className="pt-2 border-t flex justify-between font-bold">
                    <span>Total</span>
                    <span className="text-primary">R${selectedOrder.total.toFixed(2)}</span>
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