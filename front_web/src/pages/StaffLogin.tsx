import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { toast } from 'sonner';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { authService } from '@/services/auth.service';

import userIcon from "/imgs/input-icons/user_icon.svg";
import lockIcon from "/imgs/input-icons/lock_icon.svg";

const StaffLogin = () => {
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await authService.login({ email, password });
      
      // Salva o token e marca como autenticado
      localStorage.setItem('token', response.accessToken);      
      localStorage.setItem('staffAuth', 'true');
      
      toast.success('Login realizado com sucesso!', {
        description: `Bem-vindo, ${response.username || 'Usuário'}`,
      });
      
      navigate('/staff/dashboard');
    } catch (error: any) {
      console.error('Erro no login:', error);
      
      let errorMessage = 'Credenciais inválidas';
      let errorTitle = 'Erro no login';
      
      // Tratamento específico para ApiError do auth.service
      if (error.name === 'ApiError') {
        if (error.status === 500) {
          errorTitle = 'Erro no servidor';
          errorMessage = 'O servidor encontrou um erro interno. Verifique se o backend está configurado corretamente.';
        } else if (error.status === 401 || error.status === 403) {
          errorMessage = 'Usuário ou senha incorretos';
        } else if (error.status === 400) {
          errorMessage = 'Dados de login inválidos';
        } else {
          errorMessage = error.message;
        }
      } else if (error instanceof TypeError && error.message.includes('Failed to fetch')) {
        errorTitle = 'Erro de conexão';
        errorMessage = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando na porta 8000.';
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      toast.error(errorTitle, {
        description: errorMessage,
        duration: 5000,
      });
    } finally {
      setLoading(false);
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

            <div className="relative w-full">
              <img
                src={userIcon}
                alt="User icon"
                aria-hidden="true"
                className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 opacity-70"
              />

              <Input
                id="admin-email"
                type="text"
                value={email}
                placeholder="Nome de usuário"
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>

            <div className="relative w-full">
              <img
                src={lockIcon}
                alt="Lock icon"
                aria-hidden="true"
                className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 opacity-70"
              />

              <Input
                id="admin-password"
                type="text"
                value={password}
                placeholder="Senha"
                onChange={(e) => setPassword(e.target.value)}
                maxLength={8}
                required
              />
            </div>
            <p className="text-xs text-muted-foreground">
              Insira suas credenciais de acesso
            </p>
          </div>

          <Button type="submit" size="lg" className="w-full" disabled={loading}>
            {loading ? 'Acessando...' : 'Acessar Painel'}
          </Button>
        </form>
      </div>
    </div>
  );
};

export default StaffLogin;