import * as React from "react";
import { Order }  from "@/types/order";
import { Card }   from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { OrderStatusBadge } from "@/components/client/OrderStatusBadge";
import { Clock, User, GraduationCap, ShoppingCart } from "lucide-react";
import { differenceInMinutes } from "date-fns";

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

export const OrderCard: React.FC<OrderCardProps> = ({
  order,
  onStatusChange,
  onViewDetails,
}) => {
  const getNextStatus = (currentStatus: Order["status"]): Order["status"] | null => {
    const statusFlow: Order["status"][] = ["received", "preparing", "ready", "delivered", "canceled"];
    const currentIndex = statusFlow.indexOf(currentStatus);

    return currentIndex < 0 || currentIndex >= statusFlow.length - 1
      ? null
      : statusFlow[currentIndex + 1];
  };

  const nextStatus      = getNextStatus(order.status);
  const nextStatusLabel = nextStatus ? statusMessages[nextStatus] : null;

  return (
    <Card className="px-7 py-6 hover:shadow-md transition-shadow w-full">
      <div className="space-y-3 w-full">
        <div className="flex items-start justify-between border-b border-dotted">
          <div>
            <div className="text-3xl font-bold text-card-primary mb-3">
              {order.orderCode}
            </div>

            <div className="flex items-center gap-2 text-sm text-muted-foreground mb-3 whitespace-nowrap">
              <Clock className="h-3 w-3" />
              Esperando há {differenceInMinutes(new Date(), new Date(order.createdAt))} minutos
            </div>
          </div>

          <OrderStatusBadge status={order.status} />
        </div>

        <div className="space-y-2">
          <div className="flex items-center gap-2 text-sm">
            <User className="h-4 w-4 text-muted-foreground" />
            <span className="font-medium">{order.student.name}</span>
          </div>

          <div className="flex items-center justify-between text-sm">
            <div className="flex items-center gap-2">
              <GraduationCap className="h-4 w-4 text-muted-foreground" />
              <p className="text-muted-foreground">Retirada</p>
            </div>
            <p className="text-muted-foreground">{order.pickUpTime}</p>
          </div>

          <div className="flex items-center justify-between text-sm">
            <div className="flex items-center gap-2">
              <ShoppingCart className="h-4 w-4 text-muted-foreground" />
              <p className="text-muted-foreground">{order.items.length} item(s)</p>
            </div>
            <p className="text-muted-foreground">R${order.total.toFixed(2)}</p>
          </div>
        </div>

        <div className="pt-2 border-t border-dotted">
          <div className="flex gap-2">
            <Button
              size="sm"
              variant="outline"
              onClick={() => onViewDetails(order)}
              className="flex-1"
            >
              Ver Detalhes
            </Button>

            {nextStatus && nextStatus !== 'canceled' && (
              <Button
                size="sm"
                variant={nextStatus !== 'received' ? nextStatus : 'default'}
                icon={<img src="../../imgs/button-icons/chefs-hat-icon.svg" alt="" className="h-6 w-6 object-contain" />}
                onClick={() => onStatusChange(order.id, nextStatus)}
                className="flex-1"
              >
                {`${nextStatusLabel}`}
              </Button>
            )}
          </div>
        </div>
      </div>
    </Card>
  );
}