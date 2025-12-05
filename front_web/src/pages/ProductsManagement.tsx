import { useState, useMemo } from 'react';
import { Button } from '@/components/ui/button';
import { Switch } from '@/components/ui/switch';
import { Plus, Pencil, Trash2, Eye, Search } from 'lucide-react';
import {
  Table,
  TableRow,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
} from '@/components/ui/table';
import {
  Dialog,
  DialogTitle,
  DialogFooter,
  DialogHeader,
  DialogContent,
  DialogDescription,
} from '@/components/ui/dialog';
import {
  AlertDialog,
  AlertDialogTitle,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogContent,
  AlertDialogDescription,
} from '@/components/ui/alert-dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import {
  Select,
  SelectItem,
  SelectValue,
  SelectTrigger,
  SelectContent,
} from '@/components/ui/select';
import { toast } from 'sonner';
import { MenuItem } from '@/types/order';
import { mockMenuItems, categories as defaultCategories } from '@/lib/mockData';
import { CategoryDialog } from '@/components/CategoryDialog';

const ProductsManagement = () => {
  const [products, setProducts] = useState<MenuItem[]>(mockMenuItems);
  const [categories, setCategories] = useState<string[]>(defaultCategories);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All');

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
  
  const [isAddEditDialogOpen, setIsAddEditDialogOpen] = useState(false);
  const [isUnsavedChangesDialogOpen, setIsUnsavedChangesDialogOpen] = useState(false);
  const [isDetailsDialogOpen, setIsDetailsDialogOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<MenuItem | null>(null);
  const [editMode, setEditMode] = useState(false);
  
  const emptyFormData = {
    name: '',
    description: '',
    price: '',
    category: '',
    image: '',
  };

  const [formData, setFormData] = useState(emptyFormData);
  const [initialFormData, setInitialFormData] = useState(emptyFormData);

  const hasUnsavedChanges = () => {
    return (
      formData.name !== initialFormData.name ||
      formData.description !== initialFormData.description ||
      formData.price !== initialFormData.price ||
      formData.category !== initialFormData.category ||
      formData.image !== initialFormData.image
    );
  };

  const handleDialogClose = (open: boolean) => {
    if (!open && hasUnsavedChanges()) {
      setIsUnsavedChangesDialogOpen(true);
    } else {
      setIsAddEditDialogOpen(open);
    }
  };

  const handleDiscardChanges = () => {
    setIsUnsavedChangesDialogOpen(false);
    setIsAddEditDialogOpen(false);
    setFormData(emptyFormData);
    setInitialFormData(emptyFormData);
  };

  const handleStayOnForm = () => {
    setIsUnsavedChangesDialogOpen(false);
  };

  const handleAdd = () => {
    setEditMode(false);
    const newFormData = {
      name: '',
      description: '',
      price: '',
      category: '',
      image: '',
    };
    setFormData(newFormData);
    setInitialFormData(newFormData);
    setIsAddEditDialogOpen(true);
  };

  const handleEdit = (product: MenuItem) => {
    setEditMode(true);
    setSelectedProduct(product);
    const editFormData = {
      name: product.name,
      description: product.description,
      price: product.price.toString(),
      category: product.category,
      image: product.image,
    };
    setFormData(editFormData);
    setInitialFormData(editFormData);
    setIsAddEditDialogOpen(true);
  };

  const handleViewDetails = (product: MenuItem) => {
    setSelectedProduct(product);
    setIsDetailsDialogOpen(true);
  };

  const handleDeleteClick = (product: MenuItem) => {
    setSelectedProduct(product);
    setIsDeleteDialogOpen(true);
  };

  const handleDelete = () => {
    if (selectedProduct) {
      setProducts(products.filter(p => p.id !== selectedProduct.id));
      toast.success('Produto excluído com sucesso');
      setIsDeleteDialogOpen(false);
      setSelectedProduct(null);
    }
  };

  const handleToggleAvailable = (productId: string) => {
    setProducts(products.map(p => 
      p.id === productId ? { ...p, available: !p.available } : p
    ));
    const product = products.find(p => p.id === productId);
    toast.success(`Produto ${product?.available ? 'desativado' : 'ativado'}`);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (editMode && selectedProduct) {
      setProducts(products.map(p => 
        p.id === selectedProduct.id 
          ? {
              ...p,
              name: formData.name,
              description: formData.description,
              price: parseFloat(formData.price),
              category: formData.category,
              image: formData.image,
            }
          : p
      ));
      toast.success('Produto atualizado com sucesso');
    } else {
      const newProduct: MenuItem = {
        id: `product-${Date.now()}`,
        name: formData.name,
        description: formData.description,
        price: parseFloat(formData.price),
        category: formData.category,
        image: formData.image,
        available: true,
      };
      setProducts([...products, newProduct]);
      toast.success('Produto cadastrado com sucesso');
    }
    
    setIsAddEditDialogOpen(false);
    setSelectedProduct(null);
    setFormData(emptyFormData);
    setInitialFormData(emptyFormData);
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Produtos</h1>
          <p className="text-muted-foreground mt-1">
            Gerencie o cardápio do cantina
          </p>
        </div>
        <div className="flex gap-2">
          <Button onClick={handleAdd}>
            <Plus className="h-4 w-4 mr-2" />
            Adicionar Produto
          </Button>
          <CategoryDialog
            categories={categories}
            onCategoriesChange={setCategories}
          />
        </div>
      </div>

      {/* Filters */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Buscar por nome, código ou categoria..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-9"
          />
        </div>
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
      </div>

      <div className="border rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Imagem</TableHead>
              <TableHead>Nome</TableHead>
              <TableHead>Categoria</TableHead>
              <TableHead>Preço</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="text-right">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredProducts.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} className="text-center py-8 text-muted-foreground">
                  Nenhum produto encontrado
                </TableCell>
              </TableRow>
            ) : null}
            {filteredProducts.map((product) => (
              <TableRow key={product.id}>
                <TableCell>
                  <img 
                    src={product.image} 
                    alt={product.name}
                    className="w-12 h-12 object-cover rounded"
                  />
                </TableCell>
                <TableCell className="font-medium">{product.name}</TableCell>
                <TableCell>{product.category}</TableCell>
                <TableCell>${product.price.toFixed(2)}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-2">
                    <Switch
                      checked={product.available}
                      onCheckedChange={() => handleToggleAvailable(product.id)}
                    />
                    <span className="text-sm text-muted-foreground">
                      {product.available ? 'Ativo' : 'Inativo'}
                    </span>
                  </div>
                </TableCell>
                <TableCell className="text-right">
                  <div className="flex justify-end gap-2">
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => handleViewDetails(product)}
                    >
                      <Eye className="h-4 w-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => handleEdit(product)}
                    >
                      <Pencil className="h-4 w-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="icon"
                      onClick={() => handleDeleteClick(product)}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {/* Add/Edit Dialog */}
      <Dialog open={isAddEditDialogOpen} onOpenChange={handleDialogClose}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {editMode ? 'Editar Produto' : 'Adicionar Produto'}
            </DialogTitle>
            <DialogDescription>
              {editMode 
                ? 'Atualize as informações do produto' 
                : 'Preencha os dados do novo produto'}
            </DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSubmit}>
            <div className="space-y-4 py-4">
              <div className="space-y-2">
                <Label htmlFor="name">Nome</Label>
                <Input
                  id="name"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="description">Descrição</Label>
                <Textarea
                  id="description"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="price">Preço</Label>
                <Input
                  id="price"
                  type="number"
                  step="0.01"
                  value={formData.price}
                  onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="category">Categoria</Label>
                <Select
                  value={formData.category}
                  onValueChange={(value) => setFormData({ ...formData, category: value })}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Selecione uma categoria" />
                  </SelectTrigger>
                  <SelectContent>
                    {categories.filter(c => c !== 'All').map(category => (
                      <SelectItem key={category} value={category}>
                        {category}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="image">URL da Imagem</Label>
                <Input
                  id="image"
                  type="url"
                  value={formData.image}
                  onChange={(e) => setFormData({ ...formData, image: e.target.value })}
                  required
                />
              </div>
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => handleDialogClose(false)}>
                Cancelar
              </Button>
              <Button type="submit">
                {editMode ? 'Salvar' : 'Adicionar'}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      {/* Unsaved Changes Confirmation Dialog */}
      <AlertDialog open={isUnsavedChangesDialogOpen} onOpenChange={setIsUnsavedChangesDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Alterações não salvas</AlertDialogTitle>
            <AlertDialogDescription>
              Existem alterações não salvas no formulário. Deseja sair sem salvar?
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={handleStayOnForm}>
              Permanecer no formulário
            </AlertDialogCancel>
            <AlertDialogAction onClick={handleDiscardChanges}>
              Sair sem salvar
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Details Dialog */}
      <Dialog open={isDetailsDialogOpen} onOpenChange={setIsDetailsDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Detalhes do Produto</DialogTitle>
          </DialogHeader>
          {selectedProduct && (
            <div className="space-y-4">
              <img 
                src={selectedProduct.image} 
                alt={selectedProduct.name}
                className="w-full h-48 object-cover rounded-lg"
              />
              <div className="space-y-2">
                <div>
                  <Label className="text-muted-foreground">Nome</Label>
                  <p className="text-lg font-medium">{selectedProduct.name}</p>
                </div>
                <div>
                  <Label className="text-muted-foreground">Descrição</Label>
                  <p>{selectedProduct.description}</p>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label className="text-muted-foreground">Preço</Label>
                    <p className="text-lg font-bold text-primary">
                      ${selectedProduct.price.toFixed(2)}
                    </p>
                  </div>
                  <div>
                    <Label className="text-muted-foreground">Categoria</Label>
                    <p>{selectedProduct.category}</p>
                  </div>
                </div>
                <div>
                  <Label className="text-muted-foreground">Status</Label>
                  <p className={selectedProduct.available ? 'text-green-600' : 'text-red-600'}>
                    {selectedProduct.available ? 'Ativo' : 'Inativo'}
                  </p>
                </div>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>

      {/* Delete Confirmation Dialog */}
      <AlertDialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirmar Exclusão</AlertDialogTitle>
            <AlertDialogDescription>
              Tem certeza que deseja excluir o produto "{selectedProduct?.name}"? 
              Esta ação não pode ser desfeita.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">
              Excluir
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
};

export default ProductsManagement;
