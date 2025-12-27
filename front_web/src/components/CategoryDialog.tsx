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
  const [deleteConfirmation, setDeleteConfirmation] = useState<{ isOpen: boolean; categoryName: string } | null>(null);
  const [editConfirmation, setEditConfirmation] = useState<{ isOpen: boolean; index: number } | null>(null);

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
    setEditConfirmation({ isOpen: true, index });
  };

  const confirmEditCategory = () => {
    if (!editConfirmation) return;
    
    const index = editConfirmation.index;
    const actualIndex = categories.indexOf(editableCategories[index]);

    if(editingCategory && editingCategory.value.trim()) {
      const updated = [...categories];

      updated[actualIndex] = editingCategory.value.trim();
      onCategoriesChange(updated);
      setEditingCategory(null);
      
      toast.success('Categoria atualizada');
    }
    
    setEditConfirmation(null);
  };

  const handleDeleteCategory = (categoryName: string) => {
    setDeleteConfirmation({ isOpen: true, categoryName });
  };

  const confirmDeleteCategory = () => {
    if (!deleteConfirmation) return;
    
    onCategoriesChange(categories.filter(c => c !== deleteConfirmation.categoryName));
    toast.success('Categoria removida');
    setDeleteConfirmation(null);
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
            <div className="w-full">
              <Input
                label="Nova categoria"
                imageSrc="../../public/imgs/badge-icons/category-tag-icon.svg"
                imageAlt="Tag icon"
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
                      <div className="flex-1">
                        <Input
                          label="Editar categoria"
                          imageSrc="../../public/imgs/badge-icons/category-tag-icon.svg"
                          imageAlt="Tag icon"
                          value={editingCategory.value}
                          onChange={(e)  => setEditingCategory({ index, value: e.target.value })}
                          onKeyDown={(e) => e.key === 'Enter' && handleEditCategory(index)}
                          className="h-8"
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

      {/* Delete Category Confirmation Dialog */}
      <AlertDialog open={deleteConfirmation?.isOpen ?? false} onOpenChange={(open) => !open && setDeleteConfirmation(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirmar Exclusão</AlertDialogTitle>
            <AlertDialogDescription>
              Tem certeza que deseja remover a categoria "{deleteConfirmation?.categoryName}"? 
              Esta ação não pode ser desfeita.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction onClick={confirmDeleteCategory} className="bg-destructive hover:bg-destructive/90">
              Remover
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Edit Category Confirmation Dialog */}
      <AlertDialog open={editConfirmation?.isOpen ?? false} onOpenChange={(open) => !open && setEditConfirmation(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirmar Edição</AlertDialogTitle>
            <AlertDialogDescription>
              Tem certeza que deseja editar esta categoria? 
              Todos os produtos associados serão atualizados com o novo nome.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction onClick={confirmEditCategory}>
              Confirmar
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </Dialog>
  );
};
