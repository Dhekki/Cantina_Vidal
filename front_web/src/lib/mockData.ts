import { MenuItem, Order } from '@/types/order';

export const mockMenuItems: MenuItem[] = [
  {
    id: '1',
    name: 'Chicken Sandwich',
    description: 'Grilled chicken with lettuce, tomato, and mayo',
    price: 4.50,
    category: 'Sandwiches',
    image: '/placeholder.svg',
    available: true,
  },
  {
    id: '2',
    name: 'Caesar Salad',
    description: 'Fresh romaine with parmesan and croutons',
    price: 5.00,
    category: 'Salads',
    image: '/placeholder.svg',
    available: true,
  },
  {
    id: '3',
    name: 'Pepperoni Pizza Slice',
    description: 'Classic pepperoni on crispy crust',
    price: 3.00,
    category: 'Pizza',
    image: '/placeholder.svg',
    available: true,
  },
  {
    id: '4',
    name: 'Orange Juice',
    description: 'Freshly squeezed orange juice',
    price: 2.50,
    category: 'Drinks',
    image: '/placeholder.svg',
    available: true,
  },
  {
    id: '5',
    name: 'Chocolate Chip Cookie',
    description: 'Warm and chewy chocolate chip cookie',
    price: 1.50,
    category: 'Desserts',
    image: '/placeholder.svg',
    available: true,
  },
  {
    id: '6',
    name: 'Turkey Wrap',
    description: 'Turkey, cheese, and veggies in a tortilla',
    price: 4.75,
    category: 'Sandwiches',
    image: '/placeholder.svg',
    available: true,
  },
  {
    id: '7',
    name: 'Fruit Cup',
    description: 'Mixed seasonal fresh fruits',
    price: 3.50,
    category: 'Healthy',
    image: '/placeholder.svg',
    available: true,
  },
  {
    id: '8',
    name: 'Combo Meal',
    description: 'Sandwich + Drink + Side',
    price: 8.50,
    category: 'Combos',
    image: '/placeholder.svg',
    available: true,
  },
];

export const mockOrders: Order[] = [
  {
    id: '1',
    orderCode: 'A123',
    pickUpTime: '12:45',
    items: [
      { ...mockMenuItems[0], quantity: 1 },
      { ...mockMenuItems[3], quantity: 1 },
    ],
    student: {
      name: 'Emma Johnson'
    },
    status: 'received',
    createdAt: new Date(Date.now() - 1000 * 60 * 5),
    total: 7.00,
  },
  {
    id: '2',
    orderCode: 'B456',
    pickUpTime: '10:25',
    items: [
      { ...mockMenuItems[2], quantity: 2 },
      { ...mockMenuItems[4], quantity: 1 },
    ],
    student: {
      name: 'Liam Smith',
      // studentClass: '9B',
    },
    status: 'preparing',
    createdAt: new Date(Date.now() - 1000 * 60 * 10),
    total: 7.50,
  },
  {
    id: '3',
    orderCode: 'C789',
    pickUpTime: '11:00',
    items: [
      { ...mockMenuItems[7], quantity: 1 },
    ],
    student: {
      name: 'Sophia Williams',
      // studentClass: '11C',
    },
    status: 'ready',
    createdAt: new Date(Date.now() - 1000 * 60 * 15),
    total: 8.50,
  },
];

export const categories = [
  'All',
  'Pizza',
  'Salads',
  'Combos',
  'Drinks',
  'Healthy',
  'Desserts',
  'Sandwiches',
];