import { useState, useMemo } from 'react';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Plus, Minus, ShoppingCart, Trash2, Receipt, Search } from 'lucide-react';
import {
  Dialog,
  DialogTitle,
  DialogFooter,
  DialogHeader,
  DialogContent,
  DialogDescription,
} from '@/components/ui/dialog';
import { toast } from 'sonner';
import { MenuItem } from '@/types/order';
import { mockMenuItems, categories } from '@/lib/mockData';

interface CartItem extends MenuItem {
  quantity: number;
}

const InternalOrders = () => {
  const [products] = useState<MenuItem[]>(mockMenuItems.filter(p => p.available));
  const [cart, setCart] = useState<CartItem[]>([]);
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [searchQuery, setSearchQuery] = useState('');
  const [customerName, setCustomerName] = useState('');
  const [isConfirmDialogOpen, setIsConfirmDialogOpen] = useState(false);

  const filteredProducts = useMemo(() => {
    let result = products;
    
    // Filter by category
    if(selectedCategory !== 'All') {
      result = result.filter(p => p.category === selectedCategory);
    }
    
    // Filter by search query
    if(searchQuery.trim()) {
      const query = searchQuery.toLowerCase();
      result = result.filter(p =>
        p.name.toLowerCase().includes(query) ||
        p.id.toLowerCase().includes(query) ||
        p.category.toLowerCase().includes(query)
      );
    }
    
    return result;
  }, [products, selectedCategory, searchQuery]);

  const addToCart = (product: MenuItem) => {
    setCart(prev => {
      const existing = prev.find(item => item.id === product.id);
      if(existing) {
        return prev.map(item =>
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      }
      return [...prev, { ...product, quantity: 1 }];
    });
  };

  const updateQuantity = (productId: string, delta: number) => {
    setCart(prev => {
      return prev
        .map(item => {
          if(item.id === productId) {
            const newQuantity = item.quantity + delta;
            return newQuantity > 0 ? { ...item, quantity: newQuantity } : item;
          }
          return item;
        })
        .filter(item => item.quantity > 0);
    });
  };

  const removeFromCart = (productId: string) => {
    setCart(prev => prev.filter(item => item.id !== productId));
  };

  const clearCart = () => {
    setCart([]);
    setCustomerName('');
  };

  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  const handleFinishOrder = () => {
    if(cart.length === 0) {
      toast.error('Adicione itens ao pedido');
      return;
    }
    setIsConfirmDialogOpen(true);
  };

  const confirmOrder = () => {
    const orderCode = `#${Math.floor(Math.random() * 9000) + 1000}`;
    toast.success(`Pedido ${orderCode} registrado com sucesso!`);
    clearCart();
    setIsConfirmDialogOpen(false);
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">Pedido Interno</h1>
        <p className="text-muted-foreground mt-1">
          Registre pedidos feitos presencialmente no restaurante
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Products Section */}
        <div className="lg:col-span-2 space-y-4">
          {/* Search */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Buscar por nome, código ou categoria..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-9"
            />
          </div>

          {/* Category Filter */}
          <div className="flex gap-2 flex-wrap">
            {categories.map(category => (
              <Button
                key={category}
                variant={selectedCategory === category ? 'default' : 'outline'}
                size="sm"
                onClick={() => setSelectedCategory(category)}
              >
                {category}
              </Button>
            ))}
          </div>

          {/* Products Grid */}
          {filteredProducts.length === 0 ? (
            <div className="col-span-full text-center py-12 text-muted-foreground">
              Nenhum produto encontrado
            </div>
          ) : (
            <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
              {filteredProducts.map(product => (
                <Card
                  key={product.id}
                  className="cursor-pointer hover:border-primary transition-colors"
                  onClick={() => addToCart(product)}
                >
                  <CardContent className="p-3">
                    <img
                      src={product.image}
                      alt={product.name}
                      className="w-full h-24 object-cover rounded mb-2"
                    />
                    <h3 className="font-medium text-sm truncate">{product.name}</h3>
                    <div className="flex items-center justify-between mt-1">
                      <span className="text-primary font-bold">
                        R$ {product.price.toFixed(2)}
                      </span>
                      <Button size="sm" variant="outline" className="h-7 w-7 p-0">
                        <Plus className="h-4 w-4" />
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>

        {/* Cart Section */}
        <div className="lg:col-span-1">
          <Card className="sticky top-24">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2">
                <ShoppingCart className="h-5 w-5" />
                Carrinho
                {cart.length > 0 && (
                  <Badge variant="secondary" className="ml-auto">
                    {cart.reduce((sum, item) => sum + item.quantity, 0)} itens
                  </Badge>
                )}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Customer Name */}
              <div className="space-y-2">
                <Label htmlFor="customer">Nome do Cliente (opcional)</Label>
                <Input
                  id="customer"
                  placeholder="Ex: Mesa 5, João..."
                  value={customerName}
                  onChange={(e) => setCustomerName(e.target.value)}
                />
              </div>

              <Separator />

              {/* Cart Items */}
              {cart.length === 0 ? (
                <p className="text-center text-muted-foreground py-8">
                  Carrinho vazio
                </p>
              ) : (
                <ScrollArea className="h-64">
                  <div className="space-y-3 pr-4">
                    {cart.map(item => (
                      <div key={item.id} className="flex items-center gap-2">
                        <div className="flex-1 min-w-0">
                          <p className="text-sm font-medium truncate">{item.name}</p>
                          <p className="text-xs text-muted-foreground">
                            R$ {item.price.toFixed(2)} cada
                          </p>
                        </div>
                        <div className="flex items-center gap-1">
                          <Button
                            variant="outline"
                            size="icon"
                            className="h-7 w-7"
                            onClick={() => updateQuantity(item.id, -1)}
                          >
                            <Minus className="h-3 w-3" />
                          </Button>
                          <span className="w-8 text-center text-sm font-medium">
                            {item.quantity}
                          </span>
                          <Button
                            variant="outline"
                            size="icon"
                            className="h-7 w-7"
                            onClick={() => updateQuantity(item.id, 1)}
                          >
                            <Plus className="h-3 w-3" />
                          </Button>
                          <Button
                            size="icon"
                            variant="outline"
                            className="h-7 w-7 text-destructive"
                            onClick={() => removeFromCart(item.id)}
                          >
                            <Trash2 className="h-3 w-3" />
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                </ScrollArea>
              )}

              <Separator />

              {/* Total */}
              <div className="flex items-center justify-between text-lg font-bold">
                <span>Total</span>
                <span className="text-primary">R$ {total.toFixed(2)}</span>
              </div>

              {/* Actions */}
              <div className="flex gap-2">
                <Button
                  variant="outline"
                  className="flex-1"
                  onClick={clearCart}
                  disabled={cart.length === 0}
                >
                  Limpar
                </Button>
                <Button
                  className="flex-1"
                  onClick={handleFinishOrder}
                  disabled={cart.length === 0}
                >
                  <Receipt className="h-4 w-4 mr-2" />
                  Finalizar
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>

      {/* Confirm Dialog */}
      <Dialog open={isConfirmDialogOpen} onOpenChange={setIsConfirmDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Confirmar Pedido</DialogTitle>
            <DialogDescription>
              Revise os detalhes antes de confirmar
            </DialogDescription>
          </DialogHeader>
          
          <div className="space-y-4 py-4">
            {customerName && (
              <div>
                <Label className="text-muted-foreground">Cliente</Label>
                <p className="font-medium">{customerName}</p>
              </div>
            )}
            
            <div>
              <Label className="text-muted-foreground">Itens</Label>
              <div className="space-y-1 mt-1">
                {cart.map(item => (
                  <div key={item.id} className="flex justify-between text-sm">
                    <span>{item.quantity}x {item.name}</span>
                    <span>R$ {(item.price * item.quantity).toFixed(2)}</span>
                  </div>
                ))}
              </div>
            </div>

            <Separator />

            <div className="flex justify-between font-bold text-lg">
              <span>Total</span>
              <span className="text-primary">R$ {total.toFixed(2)}</span>
            </div>
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setIsConfirmDialogOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={confirmOrder}>
              Confirmar Pedido
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default InternalOrders;