import { Plus, Pencil, Trash2 } from 'lucide-react';
import { useState } from 'react';

import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogTitle,
  DialogFooter,
  DialogHeader,
  DialogTrigger,
  DialogContent,
  DialogDescription,
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { toast } from 'sonner';

interface CategoryDialogProps {
  categories: string[];
  onCategoriesChange: (categories: string[]) => void;
}

export const CategoryDialog = ({ categories, onCategoriesChange }: CategoryDialogProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [newCategory, setNewCategory] = useState('');
  const [editingCategory, setEditingCategory] = useState<{ index: number; value: string } | null>(null);

  // Filter out 'All' from editable categories
  const editableCategories = categories.filter(c => c !== 'All');

  const handleAddCategory = () => {
    if(!newCategory.trim()) {
      toast.error('Digite o nome da categoria');
      return;
    }
    if(categories.includes(newCategory.trim())) {
      toast.error('Categoria já existe');
      return;
    }

    onCategoriesChange([...categories, newCategory.trim()]);
    setNewCategory('');
    toast.success('Categoria adicionada');
  };

  const handleEditCategory = (index: number) => {
    const actualIndex = categories.indexOf(editableCategories[index]);

    if(editingCategory && editingCategory.value.trim()) {
      const updated = [...categories];

      updated[actualIndex] = editingCategory.value.trim();
      onCategoriesChange(updated);
      setEditingCategory(null);
      
      toast.success('Categoria atualizada');
    }
  };

  const handleDeleteCategory = (categoryName: string) => {
    onCategoriesChange(categories.filter(c => c !== categoryName));
    toast.success('Categoria removida');
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button variant="outline">
          <Plus className="h-4 w-4 mr-2" />
          Categorias
        </Button>
      </DialogTrigger>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle>Gerenciar Categorias</DialogTitle>
          <DialogDescription>
            Adicione, edite ou remova categorias de produtos
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4 py-4">
          {/* Add new category */}
          <div className="flex gap-2">
            <Input
              placeholder="Nova categoria..."
              value={newCategory}
              onChange={(e) => setNewCategory(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleAddCategory()}
            />
            <Button onClick={handleAddCategory} size="icon">
              <Plus className="h-4 w-4" />
            </Button>
          </div>

          {/* Category list */}
          <div className="space-y-2 max-h-64 overflow-y-auto">
            <Label className="text-muted-foreground text-xs">Categorias existentes</Label>
            {editableCategories.length === 0 ? (
              <p className="text-sm text-muted-foreground text-center py-4">
                Nenhuma categoria cadastrada
              </p>
            ) : (
              editableCategories.map((category, index) => (
                <div key={category} className="flex items-center gap-2 p-2 rounded border bg-muted/30">
                  {editingCategory?.index === index ? (
                    <>
                      <Input
                        value={editingCategory.value}
                        onChange={(e) => setEditingCategory({ index, value: e.target.value })}
                        onKeyDown={(e) => e.key === 'Enter' && handleEditCategory(index)}
                        className="h-8"
                        autoFocus
                      />
                      <Button size="sm" onClick={() => handleEditCategory(index)}>
                        Salvar
                      </Button>
                      <Button size="sm" variant="ghost" onClick={() => setEditingCategory(null)}>
                        Cancelar
                      </Button>
                    </>
                  ) : (
                    <>
                      <span className="flex-1 text-sm">{category}</span>
                      <Button
                        variant="ghost"
                        size="icon"
                        className="h-8 w-8"
                        onClick={() => setEditingCategory({ index, value: category })}
                      >
                        <Pencil className="h-3 w-3" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="icon"
                        className="h-8 w-8 text-destructive hover:text-destructive"
                        onClick={() => handleDeleteCategory(category)}
                      >
                        <Trash2 className="h-3 w-3" />
                      </Button>
                    </>
                  )}
                </div>
              ))
            )}
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => setIsOpen(false)}>
            Fechar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
