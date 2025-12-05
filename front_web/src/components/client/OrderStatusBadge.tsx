import { cn } from '@/lib/utils';
import { OrderStatus } from '@/types/order';
import { Clock, ChefHat, CheckCircle, Package } from 'lucide-react';

interface OrderStatusBadgeProps {
  status: OrderStatus;
  className?: string;
}

const statusConfig = {
  received: {
    label: 'Received',
    color: 'bg-status-received text-white',
    icon:  Clock,
  },
  preparing: {
    label: 'Preparing',
    color: 'bg-status-preparing text-white',
    icon:  ChefHat,
  },
  ready: {
    label: 'Ready for Pickup',
    color: 'bg-status-ready text-white',
    icon:  CheckCircle,
  },
  delivered: {
    label: 'Delivered',
    color: 'bg-status-delivered text-white',
    icon:  Package,
  },
};

export const OrderStatusBadge = ({ status, className }: OrderStatusBadgeProps) => {
  const config = statusConfig[status];
  const Icon = config.icon;

  return (
    <div className={cn('inline-flex items-center gap-2 px-3 py-1.5 rounded-full font-medium', 
         config.color, 
         className)}
    >
      <Icon className="h-4 w-4" />
      {config.label}
    </div>
  );
};