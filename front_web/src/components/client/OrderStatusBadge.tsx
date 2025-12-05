import { cn } from '@/lib/utils';
import { OrderStatus } from '@/types/order';
import { Clock, ChefHat, CheckCircle, Package } from 'lucide-react';

interface OrderStatusBadgeProps {
  status: OrderStatus;
  className?: string;
}

const statusConfig = {
  received: {
    label: 'Recebido',
    color: "bg-status-received-secondary text-status-received-primary border border-status-received-primary whitespace-nowrap",
    icon:  Clock,
  },
  preparing: {
    label: 'Em preparo',
    color: "bg-status-preparing-secondary text-status-preparing-primary border border-status-preparing-primary whitespace-nowrap",
    icon:  ChefHat,
  },
  ready: {
    label: 'Feito',
    color: "bg-status-ready-secondary text-status-ready-primary border border-status-ready-primary whitespace-nowrap",
    icon:  CheckCircle,
  },
  delivered: {
    label: 'Entregue',
    color: "bg-status-delivered-secondary text-status-delivered-primary border border-status-delivered-primary whitespace-nowrap",
    icon:  Package,
  },
};

export const OrderStatusBadge = ({ status, className }: OrderStatusBadgeProps) => {
  const config = statusConfig[status];
  const Icon = config.icon;

  return (
    <div className={cn('inline-flex items-center gap-2 px-3 py-1.5 rounded-full font-medium', config.color, className)}>
      <Icon className="h-4 w-4" />
      {config.label}
    </div>
  );
};