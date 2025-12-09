export type OrderStatus = 'received' | 'preparing' | 'ready' | 'delivered' | 'canceled';

export interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number;
  inStock: number;
  minimumStock: number;
  repositionInterval: number; 
  expirationData: string;
  availableToPickUp: number;
  specifications: string;
  category: string;
  image: string;
  available: boolean;
}

export interface CartItem extends MenuItem {
  quantity: number;
}

export interface StudentInfo {
  name: string;
  studentId?: string;
}

export interface Order {
  id: string;
  orderCode: string;
  pickUpTime: string;
  items: CartItem[];
  student: StudentInfo;
  status: OrderStatus;
  createdAt: Date;
  total: number;
}