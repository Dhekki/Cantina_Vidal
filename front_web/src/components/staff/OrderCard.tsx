import { Order } from '@/types/order';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { OrderStatusBadge } from '@/components/client/OrderStatusBadge';
import { Clock, User, GraduationCap } from 'lucide-react';
import { formatDistanceToNow } from 'date-fns';

interface OrderCardProps {
  order: Order;
  onStatusChange: (orderId: string, newStatus: Order['status']) => void;
  onViewDetails:  (order: Order) => void;
}

export const OrderCard = ({ order, onStatusChange, onViewDetails }: OrderCardProps) => {
  const getNextStatus = (currentStatus: Order['status']): Order['status'] | null => {

    const statusFlow: Order['status'][] = ['received', 'preparing', 'ready', 'delivered'];
    const currentIndex = statusFlow.indexOf(currentStatus);

    return currentIndex < statusFlow.length - 1 
           ? statusFlow[currentIndex + 1] 
           : null;
  };

  const nextStatus = getNextStatus(order.status);

  return (
    <Card className="p-4 hover:shadow-md transition-shadow">
      <div className="space-y-3">
        <div className="flex items-start justify-between">
          <div>
            <div className="text-2xl font-bold text-primary mb-1">
              {order.orderCode}
            </div>
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <Clock className="h-3 w-3" />
              {formatDistanceToNow(order.createdAt, { addSuffix: true })}
            </div>
          </div>
          <OrderStatusBadge status={order.status} />
        </div>

        <div className="space-y-1">
          <div className="flex items-center gap-2 text-sm">
            <User className="h-4 w-4 text-muted-foreground" />
            <span className="font-medium">{order.student.name}</span>
          </div>

          <div className="flex items-center gap-2 text-sm">
            <GraduationCap className="h-4 w-4 text-muted-foreground" />
            <span className="text-muted-foreground">
              Class {order.student.studentClass}
            </span>
          </div>
        </div>

        <div className="pt-2 border-t">
          <div className="text-sm text-muted-foreground mb-2">
            {order.items.length} item(s) • ${order.total.toFixed(2)}
          </div>
          
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => onViewDetails(order)}
              className="flex-1"
            >
              View Details
            </Button>

            {nextStatus && (
              <Button
                size="sm"
                onClick={() => onStatusChange(order.id, nextStatus)}
                className="flex-1"
              >
                Mark as {nextStatus}
              </Button>
            )}
          </div>
        </div>
      </div>
    </Card>
  );
};