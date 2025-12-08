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
import SearchIcon from '../../public/imgs/input-icons/search-icon.svg';

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
  const [isCategoriesActive, setIsCategoriesActive]   = useState(false)

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

  const showCategoryFilter = () => {
    setIsCategoriesActive(true);
  }

  const hideCategoryFilter = () => {
    setSelectedCategory('All');
    setIsCategoriesActive(false);
  }

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
        <div className="flex items-end gap-3 mb-4">
          <img src="../../imgs/header-menu-icon.svg" alt="Icon" className="h-9" />
          <h1 className="text-4xl font-semibold text-neutral-700">
            Novo Pedido Interno
          </h1>
        </div>
        <p className="text-base text-muted-foreground">
          Cadastre pedidos realizados diretamente no restaurante.
        </p>
      </div>

      {/* Filters */}
      <div className={`grid grid-cols-7 auto-rows-min gap-4 items-start justify-items-start ${isCategoriesActive ? '' : 'hidden'}`}>
          {categories.map(category => (
              <Button
                key={category}
                size='categorySize'
                variant={selectedCategory === category ? 'categorySelected' : 'outline'}
                onClick={() => setSelectedCategory(category)}
              >
                <img src="../../public/imgs/category-image-exemple.png" 
                    alt={`${category} icon`} className="w-8 h-8 object-contain" />
                {category}
              </Button>
          ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Products Section */}
        <div className="lg:col-span-2 space-y-4">
          <div className="flex gap-3 max-w-[600px] w-full">
            {/* Search */}
            <div className="relative w-full">
              <img
                src={SearchIcon}
                alt=""
                aria-hidden="true"
                className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 opacity-70 bg-slate-600"
              />

              <Input
                placeholder="Buscar por nome ou código."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-9"
              />
            </div>

            <Button
              onClick={isCategoriesActive ? hideCategoryFilter : showCategoryFilter}
              variant={isCategoriesActive ? 'categorySelected' : 'outline'}
              className="flex items-center px-7"
            >
              <img src="../../imgs/button-icons/tag-icon.svg"
                  alt="Tag icon" className="me-1 h-5"
              />
              Por Categoria

              {isCategoriesActive ? (
                <img
                  src="../../imgs/button-icons/x-icon.svg"
                  alt="Status icon"
                  className="ms-2 h-4"
                />
              ) : ''}
            </Button>
          </div>

          {/* Products Grid */}
          {filteredProducts.length === 0 ? (
            <div className="col-span-full text-center py-12 text-muted-foreground">
              Nenhum produto encontrado
            </div>
          ) : (
            <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
              {filteredProducts.map(product => (
                <Card
                  key={product.id}
                  className="cursor-pointer hover:border-primary transition-colors"
                  onClick={() => addToCart(product)}
                >
                  <CardContent className="p-3">
                    <div className="relative">
                      <img
                        src={product.image}
                        alt={product.name}
                        className="w-full h-36 object-cover rounded mb-2"
                      />

                      <Button size="addProductSize" variant="addProduct" className="h-10 w-10 p-0 absolute bottom-1 right-0">
                        <Plus className="h-6 w-6" />
                      </Button>
                    </div>
                    
                    <h3 className="font-medium text-md truncate">{product.name}</h3>
                    <div className="flex items-center gap-2 mt-1">
                      <span className="text-primary font-bold">
                        R$ {product.price.toFixed(2).replace('.', ',')}
                      </span>

                      <p className='text-[8px] text-foreground/40 font-medium'>●</p>

                      <span className='text-foreground/40'>{product.specifications}</span>
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
                <ShoppingCart className="h-5 w-5 me-2" />
                Carrinho
                {cart.length > 0 && (
                  <Badge variant="secondary" className="ml-auto">
                    {cart.reduce((sum, item) => sum + item.quantity, 0)} 
                    {cart.reduce((sum, item) => sum + item.quantity, 0) > 1 ? (' itens') : (' item')}
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
                  placeholder="Ex: Lucas, Marcos..."
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
                            R$ {item.price.toFixed(2)}/ unidade
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
                  <img src="../../public/imgs/button-icons/chefs-hat-icon.svg" alt="Icon" className="h-6 w-6 object-contain" />
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