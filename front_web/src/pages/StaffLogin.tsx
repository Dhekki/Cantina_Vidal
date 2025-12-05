import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Lock } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { toast } from 'sonner';

const StaffLogin = () => {
  const navigate = useNavigate();
  const [pin, setPin] = useState('');

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Simple PIN check for demo (in production, use proper auth)
    if (pin === '1234') {
      localStorage.setItem('staffAuth', 'true');
      navigate('/staff/dashboard');
    } else {
      toast.error('Invalid PIN');
    }
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="max-w-md w-full space-y-6">
        <div className="text-center">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-primary/10 rounded-full mb-4">
            <Lock className="h-8 w-8 text-primary" />
          </div>
          <h1 className="text-3xl font-bold">Staff Login</h1>
          <p className="text-muted-foreground mt-2">
            Enter your PIN to access the operational panel
          </p>
        </div>

        <form onSubmit={handleLogin} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="pin">PIN Code</Label>
            <Input
              id="pin"
              type="password"
              placeholder="Enter PIN"
              value={pin}
              onChange={(e) => setPin(e.target.value)}
              maxLength={4}
              required
            />
            <p className="text-xs text-muted-foreground">Demo PIN: 1234</p>
          </div>

          <Button type="submit" size="lg" className="w-full">
            Login
          </Button>
        </form>
      </div>
    </div>
  );
};

export default StaffLogin;
