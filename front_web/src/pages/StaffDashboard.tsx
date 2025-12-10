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
import { Button } from '@/components/ui/button';
import { FileX, Trash } from 'lucide-react';
import { OrderStatusBadge } from '@/components/client/OrderStatusBadge';

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

  interface OrderCardProps {
    order: Order;
    onStatusChange: (orderId: string, newStatus: Order["status"]) => void;
    onViewDetails:  (order: Order) => void;
  }

  const statusMessages: Record<Order["status"], string> = {
    received:  "Recebido",
    preparing: "Em preparo",
    ready:     "Feito",
    delivered: "Entregue",
    canceled:  "Cancelado",
  };


  const statusLabels = {
    received:  { singular: "recebido",   plural: "recebidos"   },
    preparing: { singular: "em preparo", plural: "em preparo"  },
    ready:     { singular: "pronto",     plural: "prontos"     },
    delivered: { singular: "entregue",   plural: "entregues"   },
    canceled:  { singular: "cancelado",  plural: "cancelados"  },
  };

  const getNextStatus = (currentStatus: OrderStatus): OrderStatus | null => {
    const statusFlow: OrderStatus[] = ['received', 'preparing', 'ready', 'delivered'];
    const currentIndex = statusFlow.indexOf(currentStatus);
    return currentIndex < statusFlow.length - 1 ? statusFlow[currentIndex + 1] : null;
  };

  const getNextStatusLabel = (currentStatus: OrderStatus): string => {
    const nextStatus = getNextStatus(currentStatus);
    if (!nextStatus) return '';
    
    const labels: Record<OrderStatus, string> = {
      received: 'Marcar como Recebido',
      preparing: 'Iniciar Preparo',
      ready: 'Marcar como Pronto',
      delivered: 'Marcar como Entregue',
      canceled: 'Cancelar',
    };
    
    return labels[nextStatus] || '';
  };

  const handleCancelOrder = (orderId: string) => {
    handleStatusChange(orderId, 'canceled');
    setSelectedOrder(null);
  };

  return (
    <div className="space-y-6">
        <div>
          <div className="flex items-end gap-3 mb-4">
            <img src="../../imgs/header-bell-icon.svg" alt="Icon" className="h-9" />
            <h1 className="text-4xl font-semibold text-neutral-700">
              Pedidos
            </h1>
          </div>
          <p className="text-base text-muted-foreground">
            Lorem ipsum dolor sit amet, consectetur adipiscing elitsed consectetur.
          </p>
        </div>

        {/* Tabs area */}
        <Tabs defaultValue="all" className="space-y-7">
          <div className="max-w-6xl">
            <TabsList className="grid w-full h-fit grid-cols-6">
              {statusTabs.map((tab) => (
                <StatusTabsTrigger
                  key={tab.value}
                  value={tab.value}
                  status={tab.value}
                  className="relative"
                >
                  {tab.label}
                  <span
                    className={`
                      ml-2 border
                      ${
                        tab.value === "all"
                          ? "border-foreground/40"
                          : `border-status-${tab.value}-primary bg-status-${tab.value}-secondary text-status-${tab.value}-primary`
                      }
                      w-6 h-6
                      rounded-full
                      flex justify-center items-center
                      text-xs font-bold text-center
                    `}
                  >
                    {tab.count}
                  </span>
                </StatusTabsTrigger>
              ))}
            </TabsList>
          </div>

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
            {orders.length === 0 && (
              <div className="text-center w-full flex flex-col items-center justify-center max-w-6xl  border border-slate-100 bg-black">
                <p className="text-slate-600 text-3xl">
                  Nenhum pedido encontrado
                </p>
              </div>
            )}
          </TabsContent>

          {(['received', 'preparing', 'ready', 'delivered', 'canceled'] as OrderStatus[]).map((status) => (
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
                <div className="text-center w-full flex flex-col items-center justify-center border border-slate-100 p-16 rounded-md space-y-3 shadow-sm">
                  <img src="../../imgs/empty-list-icon.png" alt="Empty status icon" className='h-36' />

                  <div className="space-y-2">
                    <p className="text-gray-600 text-2xl">
                      Nenhum pedido {statusLabels[status].singular}
                    </p>
                    <p className="text-base text-gray-500">
                      Você ainda não possui pedidos {statusLabels[status].plural}.
                    </p>
                  </div>
                </div>
              )}
            </TabsContent>
          ))}
        </Tabs>

      {/* Order Details Dialog */}
      <Dialog open={!!selectedOrder} onOpenChange={() => setSelectedOrder(null)}>
        <DialogContent>
          <div className="w-fit">
            <OrderStatusBadge status={selectedOrder?.status} />
          </div>
          <DialogHeader>
              <DialogTitle>
                Detalhes do Pedido - {selectedOrder?.orderCode}
              </DialogTitle>
          </DialogHeader>
          {selectedOrder && (
            <div className="space-y-3 text-foreground/90">
              <div>
                <div className="text-base space-y-3">
                  <div className="">
                    <p className="text-sm font-medium text-foreground/40">Cliente</p>
                    <p className='font-semibold text-foreground/80'>{selectedOrder.student.name}</p>
                  </div>

                  <div className="">
                    <p className="text-sm font-medium text-foreground/40">Retirada</p>
                    <p className='font-semibold text-foreground/80'>{selectedOrder.pickUpTime}</p>
                  </div>
                </div>
              </div>
              <div className='space-y-3'>
                <div className="space-y-1 py-3">
                  <h4 className="text-md font-medium text-foreground/80">
                    Itens
                  </h4>

                  {selectedOrder.items.map((item) => (
                    <div key={item.id} className="flex items-center justify-between gap-2 rounded-md border-b border-input/40 bg-background py-2 px-1 text-base">
                      <div className="flex items-center justify-center gap-2">
                        <img src="../../public/imgs/shopping-bag-icon.svg" 
                             alt="bag icon" className='h-4 opacity-60' />
                        <span>{item.quantity}x {item.name}</span>
                      </div>
                      <span>
                        R${(item.price * item.quantity).toFixed(2)}
                      </span>
                    </div>
                  ))}
                </div>

                <div className="space-y-2">
                  <div className="py-2 px-1 flex justify-between font-bold">
                    <div className="flex justify-center items-center gap-2">
                      <img src="../../public/imgs/shopping-cart-icon.svg"
                               alt="bag icon" className='h-5 opacity-90' />
                      <span className='text-xl text-foreground/70'>
                        Total
                      </span>
                    </div>

                    <span className="text-xl text-primary/90">
                      R${selectedOrder.total.toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>
              
              <div className="flex justify-center items-center gap-3">
                <Button
                  size="sm"
                  variant="dangerOutline"
                  className="flex-1"
                  onClick={() => handleCancelOrder(selectedOrder.id)}
                  disabled={selectedOrder.status === 'canceled' || selectedOrder.status === 'delivered'}
                >
                  <Trash className='h-4' />
                  Cancelar Pedido
                </Button>
                
                {(() => {
                  const nextStatus      = getNextStatus(selectedOrder.status);
                  const nextStatusLabel = getNextStatusLabel(selectedOrder.status);
                  
                  return nextStatus && nextStatus !== 'canceled' ? (
                    <Button
                      size="sm"
                      variant={nextStatus !== 'received' ? nextStatus : 'default'}
                      icon={<img src="../../imgs/button-icons/chefs-hat-icon.svg" alt="" className="h-6 w-6 object-contain" />}
                      onClick={() => {
                        handleStatusChange(selectedOrder.id, nextStatus);
                        setSelectedOrder(null);
                      }}
                      className="flex-1"
                    >
                      {`${nextStatusLabel}`}
                    </Button>
                  ) : null;
                }) ()}
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default StaffDashboard;