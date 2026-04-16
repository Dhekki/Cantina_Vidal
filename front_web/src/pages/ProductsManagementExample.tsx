/**
 * EXEMPLO DE INTEGRAÇÃO COM BACKEND
 * 
 * Este arquivo demonstra como integrar os componentes existentes com o backend.
 * Você pode adaptar o ProductsManagement.tsx seguindo este padrão.
 */

import { useState } from 'react';
import { useProducts } from '@/hooks/useProducts';
import { Product } from '@/services/products.service';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Pencil, Trash2, Plus } from 'lucide-react';

const ProductsManagementExample = () => {
  const {
    products,
    isLoading,
    createProduct,
    updateProduct,
    deleteProduct,
    isCreating,
    isUpdating,
    isDeleting,
  } = useProducts();

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [formData, setFormData] = useState<Partial<Product>>({
    name: '',
    price: 0,
    inStock: 0,
    category: [],
    description: '',
    available: true,
  });

  const handleOpenDialog = (product?: Product) => {
    if (product) {
      setEditingProduct(product);
      setFormData({
        name:        product.name,
        price:       product.price,
        inStock:     product.inStock,
        category:    product.category,
        available:   product.available,
        description: product.description,
      });
    } else {
      setEditingProduct(null);
      setFormData({
        name: '',
        price: 0,
        inStock: 0,
        category: [],
        available: true,
        description: '',
      });
    }
    setIsDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setIsDialogOpen(false);
    setEditingProduct(null);
    setFormData({
      name: '',
      price: 0,
      inStock: 0,
      category: [],
      available: true,
      description: '',
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (editingProduct) {
      // Atualizar produto existente
      updateProduct(
        { id: editingProduct.id!, data: formData },
        {
          onSuccess: () => {
            handleCloseDialog();
          },
        }
      );
    } else {
      // Criar novo produto
      createProduct(formData as Product, {
        onSuccess: () => {
          handleCloseDialog();
        },
      });
    }
  };

  const handleDelete = (id: number) => {
    if(confirm('Tem certeza que deseja excluir este produto?')) {
      deleteProduct(id);
    }
  };

  if (isLoading) {
    return <div className="p-6">Carregando produtos...</div>;
  }

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Gerenciamento de Produtos</h1>
        <Button onClick={() => handleOpenDialog()}>
          <Plus className="mr-2 h-4 w-4" />
          Novo Produto
        </Button>
      </div>

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Nome</TableHead>
            <TableHead>Categoria</TableHead>
            <TableHead>Preço</TableHead>
            <TableHead>Estoque</TableHead>
            <TableHead>Status</TableHead>
            <TableHead className="text-right">Ações</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {products.map((product) => (
            <TableRow key={product.id}>
              <TableCell className="font-medium">{product.name}</TableCell>
              <TableCell>{product.category}</TableCell>
              <TableCell>R$ {product.price.toFixed(2)}</TableCell>
              <TableCell>{product.inStock}</TableCell>
              <TableCell>
                <span
                  className={`px-2 py-1 rounded text-xs ${
                    product.available
                      ? 'bg-green-100 text-green-800'
                      : 'bg-red-100 text-red-800'
                  }`}
                >
                  {product.available ? 'Ativo' : 'Inativo'}
                </span>
              </TableCell>
              <TableCell className="text-right">
                <Button
                  variant="default"
                  size="sm"
                  onClick={() => handleOpenDialog(product)}
                  disabled={isUpdating}
                >
                  <Pencil className="h-4 w-4" />
                </Button>
                <Button
                  variant="default"
                  size="sm"
                  onClick={() => handleDelete(product.id!)}
                  disabled={isDeleting}
                >
                  <Trash2 className="h-4 w-4" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {editingProduct ? 'Editar Produto' : 'Novo Produto'}
            </DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit}>
            <div className="space-y-4">
              <div>
                <Label htmlFor="name">Nome</Label>
                <Input
                  id="name"
                  label=""
                  value={formData.name}
                  onChange={ (e) => setFormData({ ...formData, name: e.target.value }) }
                  required
                />
              </div>

              <div>
                <Label htmlFor="description">Descrição</Label>
                <Input
                  label=""
                  id="description"
                  value={formData.description || ''}
                  onChange={(e) =>
                    setFormData({ ...formData, description: e.target.value })
                  }
                />
              </div>

              <div>
                <Label htmlFor="category">Categoria</Label>
                <Input
                  label=""
                  id="category"
                  value={formData.category}
                  onChange={(e) =>
                    setFormData({ ...formData, category: [e.target.value] })
                  }
                  required
                  />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="price">Preço</Label>
                  <Input
                    label=""
                    id="price"
                    type="number"
                    step="0.01"
                    value={formData.price}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        price: parseFloat(e.target.value),
                      })
                    }
                    required
                  />
                </div>

                <div>
                  <Label htmlFor="inStock">Estoque</Label>
                  <Input
                    label=""
                    id="inStock"
                    type="number"
                    value={formData.inStock}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        inStock: parseInt(e.target.value),
                      })
                    }
                    required
                  />
                </div>
              </div>
            </div>

            <DialogFooter className="mt-6">
              <Button
                type="button"
                variant="outline"
                onClick={handleCloseDialog}
              >
                Cancelar
              </Button>
              <Button type="submit" disabled={isCreating || isUpdating}>
                {isCreating || isUpdating ? 'Salvando...' : 'Salvar'}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default ProductsManagementExample;
