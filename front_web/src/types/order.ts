export type OrderStatus = 'received' | 'preparing' | 'ready' | 'delivered';

export interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
  category: string;
  image: string;
  available: boolean;
}

export interface CartItem extends MenuItem {
  quantity: number;
}

export interface StudentInfo {
  name: string;
  studentClass: string;
  studentId?: string;
}

export interface Order {
  id: string;
  orderCode: string;
  items: CartItem[];
  student: StudentInfo;
  status: OrderStatus;
  createdAt: Date;
  total: number;
}
