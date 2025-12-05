import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { toast } from 'sonner';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';

const StaffLogin = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Simple credential validation for testing
    if(password === '1234') {
      localStorage.setItem('staffAuth', 'true');
      navigate('/staff/dashboard');
    } else {
      toast.error('Invalid PIN');
    }
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="max-w-sm w-full space-y-10 p-12 shadow-lg rounded-lg">
        <div className="text-center">
          <div className="inline-flex items-center justify-center w-24 h-24 rounded-full mb-4">
            <img src="/imgs/logo-restaurante-vidal-circle.png" alt="Logo Restaurante Vidal" />
          </div>
          <h1 className="text-3xl font-semibold text-neutral-600">Gestão Vidal</h1>
          <p className="text-muted-foreground mt-2 text-neutral-500">
            Acesse sua conta para entrar no painel administrativo.
          </p>
        </div>

        <form onSubmit={handleLogin} className="space-y-6">
          <div className="space-y-6">
            <Input
              id="admin-username"
              type="text"
              value={username}
              placeholder="Nome de usuário"
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            <Input
              id="admin-password"
              type="password"
              value={password}
              placeholder="Senha"
              onChange={(e) => setPassword(e.target.value)}
              maxLength={8}
              required
            />
            <p className="text-xs text-muted-foreground">
              Para testes: 1234
            </p>
          </div>

          <Button type="submit" size="lg" className="w-full">
            Acessar Painel
          </Button>
        </form>
      </div>
    </div>
  );
};

export default StaffLogin;