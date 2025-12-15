import { Plus, Pencil, Trash2, Save, X } from 'lucide-react';
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
import { Badge } from './ui/badge';

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
          Nova Categoria
        </Button>
      </DialogTrigger>

      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle className='text-3xl font-semibold text-foreground/80 mb-1'>
            Nova Categoria
          </DialogTitle>

          <DialogDescription className='text-base'>
            Adicione, edite ou remova categorias de produtos
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4 py-4">
          {/* Add new category */}
          <div className="flex gap-2">
            <div className="w-full relative">
              <img src="../../public/imgs/badge-icons/category-tag-icon.svg"
                   alt="Tag icon" className='absolute left-3 top-1/2 -translate-y-1/2 h-5 w-4 me-2'/>

              <Input
                placeholder="Nova categoria..."
                value={newCategory}
                onChange={(e)  => setNewCategory(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleAddCategory()}
              />
            </div>

            <Button onClick={handleAddCategory}>
              <Plus className="h-4 w-4" />
            </Button>
          </div>

          {/* Category list */}
          <div className="space-y-2 max-h-48 overflow-y-auto">
            <Label className="text-muted-foreground text-xs">
              Categorias cadastradas
            </Label>

            {editableCategories.length === 0 ? (
              <p className="text-sm text-muted-foreground text-center py-4">
                Nenhuma categoria cadastrada
              </p>
            ) : (
              editableCategories.map((category, index) => (
                <div key={category} className="flex items-center gap-2 p-2 rounded-md border-b border-input/60 bg-background text-base">
                  {editingCategory?.index === index ? (
                    <>
                      <div className="relative flex-1">
                        <img src="../../public/imgs/badge-icons/category-tag-icon.svg"
                             alt="Tag icon" className='absolute left-3 top-1/2 -translate-y-1/2 h-5 w-4 me-2'/>
                        <Input
                          value={editingCategory.value}
                          onChange={(e)  => setEditingCategory({ index, value: e.target.value })}
                          onKeyDown={(e) => e.key === 'Enter' && handleEditCategory(index)}
                          className="h-8 "
                          autoFocus
                        />
                      </div>

                      <Button
                        variant="outline"
                        size="icon"
                        className="h-8 w-8"
                        onClick={() => handleEditCategory(index)}
                      >
                        <Save className="h-3 w-3" />
                      </Button>

                      <Button
                        variant="outline"
                        size="icon"
                        className="h-8 w-8 text-destructive hover:text-destructive"
                        onClick={() => setEditingCategory(null)}
                      >
                        <X className="h-3 w-3" />
                      </Button>
                    </>
                  ) : (
                    <>
                      <div className="flex-1">
                        <Badge variant="categorie" className="w-fit">
                          <img src="../../public/imgs/badge-icons/category-tag-icon.svg"
                                alt="Tag icon" className='me-2'/>
                          <p className='font-semibold'>
                            {category}
                          </p>
                        </Badge>
                      </div>

                      <Button
                        variant="outline"
                        size="icon"
                        className="h-8 w-8"
                        onClick={() => setEditingCategory({ index, value: category })}
                      >
                        <Pencil className="h-3 w-3" />
                      </Button>

                      <Button
                        variant="outline"
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
          <Button variant="outline" className='w-full' onClick={() => setIsOpen(false)}>
            Fechar
          </Button>

          <Button variant="default" className='w-full'>
            <Plus className="h-4 w-4 mr-2" />
            Adicionar Categorias
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
