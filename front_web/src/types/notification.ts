export type NotificationType = 'new_order' | 'order_ready' | 'inventory_low' | 'system';

export interface Notification {
  id: string;
  type: NotificationType;
  title: string;
  message: string;
  timestamp: Date;
  read: boolean;
  orderId?: string;
}