import { useState, useMemo } from 'react';
import { Button } from '@/components/ui/button';
import { Switch } from '@/components/ui/switch';
import { Plus, Pencil, Trash2, Eye } from 'lucide-react';
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
  SelectTrigger, 
  SelectContent,
} from '@/components/ui/select';
import { toast } from 'sonner';
import { MenuItem } from '@/types/order';
import { mockMenuItems, categories as defaultCategories } from '@/lib/mockData';

import { CategoryDialog } from '@/components/CategoryDialog';

import { Badge } from '@/components/ui/badge';
import SearchInput from '@/components/ui/search-input';


const ProductsManagement = () => {
  const [products, setProducts]       = useState<MenuItem[]>(mockMenuItems);
  const [categories, setCategories]   = useState<string[]>(defaultCategories);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All');

  const filteredProducts = useMemo(() => {
    const query = searchQuery.trim().toLowerCase();

    return products.filter(p => {
      const categories = Array.isArray(p.category) ? p.category : [p.category];

      // Filter by category
      if(selectedCategory !== 'All' && !categories.includes(selectedCategory)) return false;

      // Filter by search query
      if(query) {
        return (
          p.name.toLowerCase().includes(query)       ||
          String(p.id).toLowerCase().includes(query) ||
          categories.some(c => c.toLowerCase().includes(query))
        );
      }

      return true;
    });
  }, [products, selectedCategory, searchQuery]);
  
  const [editMode, setEditMode] = useState(false);
  const [isCategoriesActive, setIsCategoriesActive]   = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen]   = useState(false);
  const [isAddEditDialogOpen, setIsAddEditDialogOpen] = useState(false);
  const [isDetailsDialogOpen, setIsDetailsDialogOpen] = useState(false);

  const [isUnsavedChangesDialogOpen, setIsUnsavedChangesDialogOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<MenuItem | null>(null);
  
  const emptyFormData = {
    name: '',
    description: '',
    price: '',
    stock: '',
    minimumStock: '',
    replacementInterval: '',
    category: [] as string[],
    image: '',
    dataValidade: '',
  };

  const [formData, setFormData] = useState(emptyFormData);
  const [initialFormData, setInitialFormData] = useState(emptyFormData);

  const showCategoryFilter = () => {
    setIsCategoriesActive(true);
  }

  const hideCategoryFilter = () => {
    setSelectedCategory('All');
    setIsCategoriesActive(false);
  }

  const hasUnsavedChanges = () => {
    return (
      formData.name                !== initialFormData.name                ||
      formData.description         !== initialFormData.description         ||
      formData.price               !== initialFormData.price               ||
      JSON.stringify(formData.category) !== JSON.stringify(initialFormData.category) ||
      formData.stock               !== initialFormData.stock               ||
      formData.minimumStock        !== initialFormData.minimumStock        ||
      formData.replacementInterval !== initialFormData.replacementInterval ||
      formData.dataValidade        !== initialFormData.dataValidade        ||
      formData.image               !== initialFormData.image
    );
  };

  const handleDialogClose = (open: boolean) => {
    if(!open) {
      if(hasUnsavedChanges()) {
        setIsUnsavedChangesDialogOpen(true);
      } else {
        setIsAddEditDialogOpen(false);
        setFormData(emptyFormData);
        setInitialFormData(emptyFormData);
        setSelectedProduct(null);
        setEditMode(false);
      }
    } else {
      setIsAddEditDialogOpen(true);
    }
  };

  const handleDiscardChanges = () => {
    setIsUnsavedChangesDialogOpen(false);
    setIsAddEditDialogOpen(false);
    setFormData(emptyFormData);
    setInitialFormData(emptyFormData);
    setSelectedProduct(null);
    setEditMode(false);
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
      stock: '',
      minimumStock: '',
      replacementInterval: '',
      category: [] as string[],
      image: '',
      dataValidade: '',
    };
    setFormData(newFormData);
    setInitialFormData(newFormData);
    setIsAddEditDialogOpen(true);
  };

  const handleEdit = (product: MenuItem) => {
    setEditMode(true);
    setSelectedProduct(product);

    const editFormData = {
      name:                product.name,
      description:         product.description,
      price:               product.price.toString(),
      category:            [...product.category],
      stock:               product.inStock.toString(),
      minimumStock:        product.minimumStock.toString(),
      replacementInterval: product.replacementInterval?.toString(),
      dataValidade:        product.expirationData,
      image:               product.image,
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
    if(selectedProduct) {
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
    
    if(formData.category.length === 0) {
      toast.error('Selecione pelo menos uma categoria.');
      return;
    }
    
    if(editMode && selectedProduct) {
      setProducts(products.map(p => 
        p.id === selectedProduct.id 
          ? {
              ...p,
              name:                formData.name,
              description:         formData.description,
              price:               parseFloat(formData.price),
              inStock:             parseInt(formData.stock),
              minimumStock:        parseInt(formData.minimumStock),
              replacementInterval: parseInt(formData.replacementInterval),
              expirationData:      formData.dataValidade,
              category:            formData.category,
              image:               formData.image,
            }
          : p
      ));
      toast.success('Produto atualizado com sucesso');
    } else {
      const newProduct: MenuItem = {
        id:                  `product-${Date.now()}`,
        name:                formData.name,
        description:         formData.description,
        price:               parseFloat(formData.price),
        category:            formData.category,
        image:               formData.image,
        available:           true,
        availableToPickUp:   parseInt(formData.stock),
        inStock:             parseInt(formData.stock),
        minimumStock:        parseInt(formData.minimumStock),
        replacementInterval: parseInt(formData.replacementInterval),
        expirationData:      formData.dataValidade,
        specifications:      formData.description,
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
      <div>
        <div className="flex items-end gap-3 mb-4">
          <img src="../../imgs/header-menu-icon.svg" alt="Icon" className="h-9" />
          <h1 className="text-4xl font-semibold text-neutral-700">
            Produtos Cadastrados
          </h1>
        </div>
        <p className="text-base text-muted-foreground">
          Gerencie seus produtos e pratos de forma eficiente.
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

      <div className="flex justify-between">
        <div className="flex gap-3 max-w-[600px] w-full">
          {/* Search */}
          <SearchInput searchQuery={searchQuery} setSearchQuery={setSearchQuery} />

          {/* Search By Category Button */}
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

      <div className="border rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Imagem</TableHead>
              <TableHead>Nome</TableHead>
              <TableHead>Descrição</TableHead>
              <TableHead>Categorias</TableHead>
              <TableHead>Preço</TableHead>
              <TableHead>Em Estoque</TableHead>
              <TableHead>Disponíveis</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="text-center">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredProducts.length === 0 ? (
              <TableRow>
                <TableCell
                  colSpan={9}
                  className="py-8 text-muted-foreground "
                >
                  <div className="flex flex-col items-center justify-center text-center">
                    <img
                      src="/imgs/products-empty-list-icon.png"
                      alt="Empty list icon"
                      className="mb-3"
                    />
                    <p className="text-gray-600 text-2xl">
                      Nenhum produto encontrado
                    </p>
                  </div>
                </TableCell>
              </TableRow>
            ) : null}
            {filteredProducts.map((product) => (
              <TableRow key={product.id}>
                <TableCell>
                  <img 
                    src={product.image} 
                    alt={product.name}
                    className="w-fit h-12 object-cover rounded"
                  />
                </TableCell>
                <TableCell className="font-medium">{product.name}</TableCell>

                <TableCell>{product.description}</TableCell>

                <TableCell>
                  <div className="flex items-center justify-start gap-2">
                    {product.category.map((catg, idx) => (
                    <Badge key={idx} variant="categorie" className="py-1">
                      <img src="../../public/imgs/badge-icons/category-tag-icon.svg"
                           alt="Tag icon" className='me-2'/>
                    
                      <p className='font-semibold'>{catg}</p>
                    </Badge>
                    ))}
                  </div>
                </TableCell>
                
                <TableCell className='text-end'>R${product.price.toFixed(2)}</TableCell>
                <TableCell className='text-end'>{product.inStock}</TableCell>
                <TableCell className='text-end'>{product.availableToPickUp}</TableCell>
                
                <TableCell>
                  <div className="flex items-center gap-2 w-[120px]">
                    <Switch
                      checked={product.available}
                      onCheckedChange={() => handleToggleAvailable(product.id)}
                    />
                    <span className="text-sm text-muted-foreground">
                      {product.available ? 'Ativo' : 'Inativo'}
                    </span>
                  </div>
                </TableCell>
                
                <TableCell>
                  <div className="flex justify-center gap-2">
                    <Button
                      variant="outline"
                      size="icon"
                      onClick={() => handleViewDetails(product)}
                    >
                      <Eye className="h-4 w-4" />
                    </Button>

                    <Button
                      variant="outline"
                      size="icon"
                      onClick={() => handleEdit(product)}
                    >
                      <Pencil className="h-4 w-4" />
                    </Button>

                    <Button
                      variant="outline"
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
        <DialogContent className='max-w-[608px] w-full py-12 px-10'>
          <DialogHeader>
            <DialogTitle className='text-3xl text-foreground/80'>
              {editMode ? 'Editar Produto' : 'Cadastrar Produto'}
            </DialogTitle>

            <DialogDescription className='text-sm'>
              {editMode 
                ? 'Atualize as informações do produto' 
                : 'Preencha os dados para cadastrar um novo produto.'}
            </DialogDescription>
          </DialogHeader>
          
          <form onSubmit={handleSubmit}>
            <div className="space-y-6 py-4">
              {/* Row: Product Name / Expiration date */}
              <div className="flex gap-4">
                {/* Input: Product name */}
                <div className="space-y-2 w-full">
                    <Input
                      id="name"
                      imageSrc = '../../public/imgs/input-icons/box-icon.svg'
                      imageAlt='Box icon'
                      label='Nome do produto'
                      autoComplete='off'
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                      required
                    />
                </div>
                
                {/* Input: Expiration Date */}
                <div className="space-y-2 w-full">
                    <Input
                      id="dataValidade"
                      type='date'
                      placeholder='Data de validade'
                      imageSrc = '../../public/imgs/input-icons/calendar-icon.svg'
                      imageAlt='Calendar icon'
                      label='Data de validade'
                      value={formData.dataValidade}
                      onChange={(e) => setFormData({ ...formData, dataValidade: e.target.value })}
                      required
                    />
                </div>
              </div>
              {/* Row: Product Price / Quantity in Stock */}
              <div className="flex gap-4">
                {/* Input: Product Price */}
                <div className="space-y-2 w-full">
                  <div className="relative">

                    <Input
                      id="price"
                      type='number'
                      step={'0.01'}
                      imageSrc = '../../public/imgs/input-icons/reais-icon.svg'
                      imageAlt='Price icon'
                      label='Preço'
                      value={formData.price}
                      onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                      required
                    />
                  </div>
                </div>
                
                {/* Input: Quantity in Stock */}
                <div className="space-y-2 w-full">
                  {/* <Label htmlFor="inStock">Quantidade em Estoque</Label> */}
                  <div className="relative">

                    <Input
                      id="inStock"
                      type='number'
                      step={'1'}
                      imageSrc = '../../public/imgs/input-icons/boxes-icon.svg'
                      imageAlt='Boxes icon'
                      label='Quantidade'
                      autoComplete='off'
                      value={formData.stock}
                      onChange={(e) => setFormData({ ...formData, stock: e.target.value })}
                      required
                    />
                  </div>
                </div>
              </div>

              {/* Row: Minimum Stock / Replacement Interval */}
              <div className="flex gap-4">
                {/* Input: Minimum Stock */}
                <div className="space-y-2 w-full">
                  <div className="relative">
                    {/* <img
                      src="../../public/imgs/input-icons/stock-icon.svg"
                      alt="User icon"
                      aria-hidden="true"
                      className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 opacity-70"
                    /> */}

                    <Input
                      id="minimumStock"
                      type='number'
                      step={'1'}
                      imageSrc = '../../public/imgs/input-icons/stock-icon.svg'
                      imageAlt='Stock icon'
                      label='Estoque mínimo'
                      autoComplete='off'
                      value={formData.minimumStock}
                      onChange={(e) => setFormData({ ...formData, minimumStock: e.target.value })}
                      required
                    />
                  </div>
                </div>
                
                {/* Input: Replacement Interval */}
                <div className="space-y-2 w-full">
                  <div className="relative">
                    <img
                      src="../../public/imgs/input-icons/boxes-icon.svg"
                      alt="User icon"
                      aria-hidden="true"
                      className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 opacity-70"
                    />

                    <Input
                      id="replacementInterval"
                      type='number'
                      step={'1'}
                      label='Intervalo de Reposição (dias)'
                      imageSrc = '../../public/imgs/input-icons/boxes-icon.svg'
                      imageAlt='Boxes icon'
                      autoComplete='off'
                      value={formData.replacementInterval}
                      onChange={(e) => setFormData({ ...formData, replacementInterval: e.target.value })}
                      required
                    />
                  </div>
                </div>
              </div>

              <div className="space-y-2">
                <div className="relative">
                  <div className="absolute left-3 top-4 -translate-y-1/2 flex items-center gap-2">
                    <img
                      src="../../public/imgs/input-icons/notes-icon.svg"
                      alt="User icon"
                      aria-hidden="true"
                      className=" h-5 w-5 opacity-70"
                    />

                    <p className='text-base text-muted-foreground mt-1'>
                      Descrição
                    </p>
                  </div>

                  <Textarea
                    id="description"
                    placeholder='Faça uma breve descrição do seu produto.'
                    className='h-[132px]'
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    required
                  />
                </div>

                <div className="flex justify-between items-center text-sm font-semibold text-muted-foreground/60 w-full">
                  <p>A descrição ficará visível no app de vendas.</p>
                  <p>
                    <span id='caracter-counter text-'>
                      {formData.description.length}
                    </span>/500
                  </p>
                </div>
              </div>

              <div className="flex gap-4 w-full">
                {/* Input: Images Upload */}
                {!formData.image ? (
                  <div className="space-y-2 w-full">
                    <label 
                      htmlFor="image-upload"
                      className="flex flex-col justify-center items-center border border-input/90 px-5 py-8 gap-2 rounded-md hover:bg-muted/30 cursor-pointer"
                    >
                      <img src="../../public/imgs/input-icons/upload-file-icon.svg" 
                            alt="Upload file icon" 
                            className='h-8 w-8'
                      />

                      <p className='text-sm text-center font-medium text-foreground/50'>
                        {formData.image ? 'Imagem selecionada' 
                                  : 'Arraste o arquivo aqui ou clique para selecionar'}
                      </p>
                    </label>

                    <Input
                      id="image-upload"
                      type="file"
                      label='Quantidade'
                      accept="image/*"
                      className="hidden"
                      onChange={(e) => {
                        const file = e.target.files?.[0];
                        
                        if(file) {
                          const reader = new FileReader();
                          reader.onloadend = () => {
                            setFormData({ ...formData, image: reader.result as string });
                          };
                          
                          reader.readAsDataURL(file);
                        }
                      }}
                    />
                  </div>
                ) : (
                  <div className="border border-input/90 px-5 py-5 gap-2 rounded-md hover:bg-muted/30 cursor-pointer relative" >
                    <div id='image-overflow'></div>

                    <div className="w-[216px] h-[216px]">
                      <img src={formData.image} 
                          alt="Image uploaded" className='object-cover object-center w-full h-full shadow rounded-[2px]' onMouseOver={() =>{}} />
                    </div>
                  </div>
                )}

                {/* Input: Category */}
                <div id='categories-container' className="space-y-4 w-full">
                  <Select
                    value=""
                    onValueChange={(value) => {
                      if(value && !formData.category.includes(value)) {
                        setFormData({ ...formData, category: [...formData.category, value] });
                      }
                    }}
                  >
                    <SelectTrigger>
                      <div className="relative">
                        <img
                          src="../../public/imgs/button-icons/tag-icon.svg"
                          alt="User icon"
                          aria-hidden="true"
                          className="absolute top-1/2 h-5 w-5 -translate-y-1/2 opacity-70"
                        />

                        <Label className='font-normal pl-7'>
                          Categorias
                        </Label>
                      </div>
                    </SelectTrigger>

                    <SelectContent>
                      {categories.filter(c => c !== 'All').map(category => (
                        <SelectItem key={category} value={category}>
                          {category}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>

                  {/* Selected Categories Badges */}
                  {formData.category.length > 0 ? (
                    <div className="flex flex-wrap gap-2 mt-2">
                      {formData.category.map((categ) => (
                        <Badge key={categ} variant="categorie" className="px-3">
                          <img 
                            src="../../public/imgs/badge-icons/category-tag-icon.svg"
                            alt="Tag icon" className='me-2 h-4 w-4'
                          />
                          
                          <span className='font-semibold text-sm'>
                            {categ}
                          </span>

                          <button
                            type="button"
                            onClick={() => {
                              setFormData({
                                ...formData,
                                category: formData.category.filter(c => c !== categ)
                              });
                            }}
                            className="ml-2 hover:bg-foreground/10 rounded-full p-0.5"
                          >
                            <img 
                              src="../../imgs/badge-icons/remove-category-icon.svg"
                              alt="Remove category"
                              className='h-3 w-3'
                            />
                          </button>
                        </Badge>
                      ))}
                    </div>
                  ) : (
                    <p className="text-sm text-muted-foreground/60 mt-2">
                      Selecione uma ou mais categorias acima
                    </p>
                  )}
                </div>
              </div>
            </div>

            <DialogFooter className='h-fit'>
              <Button 
                type="button" 
                variant="outline" 
                className="w-full py-6"
                onClick={() => handleDialogClose(false)}
              >
                Cancelar
              </Button>
              
              <Button 
                type="submit"
                onClick={() => {
                  console.log(formData)
                }}
                className="w-full py-6"
              >
                {editMode ? 'Salvar' : 'Adicionar'}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      {/* Unsaved Changes Confirmation Dialog */}
      <AlertDialog open={isUnsavedChangesDialogOpen} onOpenChange={setIsUnsavedChangesDialogOpen}>
        <AlertDialogContent>
          {/* Dialog Icon */}
          <img src="../../public/imgs/pop-ups-icons/edit-icon.png" alt="Edit icon" />

          {/* Dialog Header */}
          <AlertDialogHeader>
            <AlertDialogTitle>
              Alterações não salvas
            </AlertDialogTitle>

            <AlertDialogDescription>
              Existem alterações não salvas no formulário. Deseja sair sem salvar?
            </AlertDialogDescription>
          </AlertDialogHeader>
          
          {/* Unsaved Changes Options */}
          <AlertDialogFooter className='w-full'>
            <AlertDialogCancel onClick={handleStayOnForm} className='w-full'>
              Cancelar
            </AlertDialogCancel>

            <AlertDialogAction onClick={handleDiscardChanges} className='w-full'>
              Sair sem salvar
            </AlertDialogAction>
          </AlertDialogFooter>

        </AlertDialogContent>
      </AlertDialog>

      {/* Details Dialog */}
      <Dialog open={isDetailsDialogOpen} onOpenChange={setIsDetailsDialogOpen}>
        <DialogContent className='max-w-[810px] w-full h-fit py-12 px-10'>
          {selectedProduct && (
            <div className="flex gap-11">

              <div className="border border-input rounded-xl p-5 min-w-[352px] min-h-[352px] w-full h-full">
                <img
                  src={selectedProduct.image}
                  alt={selectedProduct.name}
                  className="w-full h-full object-cover shadow rounded-sm"
                />
              </div>

              <div className="space-y-2 w-full">
                <div>
                  <Badge variant={selectedProduct.available ? 'active' : 'inactive'} 
                         className="mb-2">
                    <img src={`../../public/imgs/badge-icons/product-${selectedProduct.available 
                                                                       ? 'active' : 'inactive'}-icon.svg`} 
                         alt={(selectedProduct.available ? 'active' : 'inactive') + 'icon'}
                         className='me-1'
                    />
                    {selectedProduct.available ? 'Ativo' : 'Inativo'}
                  </Badge>
                </div>

                <DialogHeader>
                  <DialogTitle className='text-foreground/80 mb-2'>
                    Detalhes do Produto
                  </DialogTitle>
                </DialogHeader>

                <div className="w-full flex flex-col gap-4">
                  <div>
                    <Label className="text-sm font-medium text-foreground/40">
                      Nome
                    </Label>
                    <p className="font-semibold text-foreground/80">
                      {selectedProduct.name}
                    </p>
                  </div>

                  <div>
                    <Label className="text-sm font-medium text-foreground/40">
                      Descrição
                    </Label>
                    <p className="font-semibold text-foreground/80">
                      {selectedProduct.description}
                    </p>
                  </div>

                  <div className="flex justify-between w-full">
                    <div>
                      <Label className="text-sm font-medium text-foreground/40">
                        Preço
                      </Label>
                      <p className="font-semibold text-foreground/80">
                        R${selectedProduct.price.toFixed(2).replace('.', ',')}
                      </p>
                    </div>

                    <div>
                      <Label className="text-sm font-medium text-foreground/40">
                        Quantidade
                      </Label>
                      <p className="font-semibold text-foreground/80 truncate">
                        {selectedProduct.inStock} unidades
                      </p>
                    </div>

                    <div>
                      <Label className="text-sm font-medium text-foreground/40">
                        Data de Validade
                      </Label>
                      <p className="font-semibold text-foreground/80">
                        {new Date(selectedProduct.expirationData).toLocaleDateString()}
                      </p>
                    </div>
                  </div>

                  <div className='flex flex-col gap-2'>
                    <Label className="text-sm font-medium text-foreground/40">
                      Categorias
                    </Label>
                    
                    <div className="flex gap-2">
                      {selectedProduct.category.map(catg => (
                        <Badge key={catg} variant="categorie" className="w-fit">
                          <img src="../../public/imgs/badge-icons/category-tag-icon.svg"
                                alt="Tag icon" className='me-2'/>
                      
                          <p className='font-semibold'>
                            {catg}
                          </p>
                        </Badge>
                      ))}
                    </div>
                  </div>
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
            <AlertDialogTitle>
              Confirmar Exclusão
            </AlertDialogTitle>

            <AlertDialogDescription>
              Tem certeza que deseja excluir o produto "{selectedProduct?.name}"? 
              Esta ação não pode ser desfeita.
            </AlertDialogDescription>

          </AlertDialogHeader>

          <AlertDialogFooter>
            <AlertDialogCancel>
              Cancelar
            </AlertDialogCancel>

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
