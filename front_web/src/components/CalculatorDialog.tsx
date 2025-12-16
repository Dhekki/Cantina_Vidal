import { Calculator } from 'lucide-react';
import { useState, useEffect, useCallback, ReactNode } from 'react';

import {
  Dialog,
  DialogTitle,
  DialogHeader,
  DialogTrigger,
  DialogContent,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';

export const CalculatorDialog = ({ children }: { children?: ReactNode }) => {
  const [display, setDisplay]                     = useState('0');
  const [operator, setOperator]                   = useState(null);
  const [previousValue, setPreviousValue]         = useState(null);
  const [waitingForOperand, setWaitingForOperand] = useState(false);
  const [isOpen, setIsOpen]                       = useState(false);

  const formatDisplay = (value) => {
    const num = parseFloat(value);

    if(isNaN(num)) return value;
    
    const str = String(num);
    
    if(str.length > 15) {
      if(Math.abs(num) >= 1e10 || (Math.abs(num) < 0.0001 && num !== 0))
        return num.toExponential(6);
        
      return num.toFixed(8).replace(/\.?0+$/, '');
    }
    return value;
  };

  const inputDigit = useCallback((digit) => {
    if(waitingForOperand) {
      setDisplay(digit);
      setWaitingForOperand(false);
    } else {
      const newDisplay = display === '0' ? digit : display + digit;

      if(newDisplay.length <= 15) setDisplay(newDisplay);
    }
  }, [display, waitingForOperand]);

  const inputDecimal = useCallback(() => {
    if(waitingForOperand) {
      setDisplay('0.');
      setWaitingForOperand(false);
      return;
    }

    if(!display.includes('.')) setDisplay(display + '.');
  }, [display, waitingForOperand]);

  const clear = useCallback(() => {
    setDisplay('0');
    setPreviousValue(null);
    setOperator(null);
    setWaitingForOperand(false);
  }, []);

  const backspace = useCallback(() => {
    display.length > 1 ? setDisplay(display.slice(0, -1)) 
                       : setDisplay('0');
  }, [display]);

  const calculate = (a, b, op) => {
    switch (op) {
      case '+': return a + b;
      case '-': return a - b;
      case '×':
      case '*': return a * b;
      case '÷':
      case '/': return b !== 0 ? a / b : 0;
      default: return b;
    }
  };

  const performOperation = useCallback((nextOperator) => {
    const inputValue = parseFloat(display);

    if(previousValue === null) {
      setPreviousValue(inputValue);
    } else if(operator) {
      const result = calculate(previousValue, inputValue, operator);
      const formattedResult = formatDisplay(String(result));

      setDisplay(formattedResult);
      setPreviousValue(result);
    }

    setWaitingForOperand(true);
    setOperator(nextOperator);
  }, [display, operator, previousValue]);

  const handleEquals = useCallback(() => {
    if (operator && previousValue !== null) {
      const inputValue = parseFloat(display);
      const result = calculate(previousValue, inputValue, operator);
      const formattedResult = formatDisplay(String(result));

      setDisplay(formattedResult);
      setOperator(null);
      setPreviousValue(null);
      setWaitingForOperand(true);
    }
  }, [display, operator, previousValue]);

  const handlePercent = useCallback(() => {
    const value = parseFloat(display);
    const result = value / 100;

    setDisplay(formatDisplay(String(result)));
  }, [display]);

  const handlePlusMinus = useCallback(() => {
    const value = parseFloat(display);
    setDisplay(String(value * -1));
  }, [display]);

  // Keyboard event handler
  useEffect(() => {
    if(!isOpen) return;

    const handleKeyDown = (e) => {
      e.preventDefault();

      // Numbers
      if(e.key >= '0' && e.key <= '9') {
        inputDigit(e.key);
      }
      // Decimal
      else if(e.key === '.' || e.key === ',') {
        inputDecimal();
      }
      // Operators
      else if(e.key === '+') {
        performOperation('+');
      }
      else if(e.key === '-') {
        performOperation('-');
      }
      else if(e.key === '*') {
        performOperation('×');
      }
      else if(e.key === '/') {
        performOperation('÷');
      }
      // Equals
      else if(e.key === 'Enter' || e.key === '=') {
        handleEquals();
      }
      // Clear
      else if(e.key === 'Escape' || e.key.toLowerCase() === 'c') {
        clear();
      }
      // Backspace
      else if(e.key === 'Backspace') {
        backspace();
      }
      // Percent
      else if(e.key === '%') {
        handlePercent();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, inputDigit, inputDecimal, performOperation, handleEquals, clear, backspace, handlePercent]);

  const buttonClass   = "h-14 text-lg font-medium transition-all";
  const operatorClass = "bg-primary text-white hover:bg-primary/80 active:bg-primary/60";
  const functionClass = "bg-gray-200 hover:bg-gray-300 active:bg-gray-400 text-foreground/80";
  const numberClass   = "bg-gray-100 hover:bg-gray-200 active:bg-gray-300 border border-gray-300 text-foreground/80";

  const getFontSize = () => {
    const len = display.length;
    if(len > 12) return 'text-xl';
    if(len > 10) return 'text-2xl';
    if(len > 8)  return 'text-3xl';
    return 'text-4xl';
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        {children || (
          <Button size="icon" className="h-9 w-9">
            <Calculator className="h-5 w-5" />
          </Button>
        )}
      </DialogTrigger>
      
      <DialogContent className="max-w-sm p-6 pt-10">
        <DialogHeader>
          <DialogTitle className="sr-only">Calculadora</DialogTitle>
        </DialogHeader>

        <div className="space-y-3">
          {/* Display */}
          <div className="bg-gray-200 rounded-lg p-6 text-right shadow-inner min-h-[100px] flex flex-col justify-end overflow-hidden">
            <div className="text-sm text-foreground/80 h-5 mb-1 font-mono truncate">
              {operator && previousValue !== null ? `${previousValue} ${operator}` : ''}
            </div>
            <div className={`${getFontSize()} font-mono text-foreground/80 break-words leading-tight`}>
              {display}
            </div>
          </div>

          {/* Buttons */}
          <div className="grid grid-cols-4 gap-2">
            <Button className={`${buttonClass} ${functionClass}`} onClick={clear}>
              AC
            </Button>
            <Button className={`${buttonClass} ${functionClass}`} onClick={backspace}>
              ⌫
            </Button>
            <Button className={`${buttonClass} ${functionClass}`} onClick={handlePercent}>
              %
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('÷')}>
              ÷
            </Button>

            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('7')}>
              7
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('8')}>
              8
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('9')}>
              9
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('×')}>
              ×
            </Button>

            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('4')}>
              4
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('5')}>
              5
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('6')}>
              6
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('-')}>
              −
            </Button>

            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('1')}>
              1
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('2')}>
              2
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('3')}>
              3
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('+')}>
              +
            </Button>

            <Button className={`${buttonClass} ${functionClass}`} onClick={handlePlusMinus}>
              ±
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={() => inputDigit('0')}>
              0
            </Button>
            <Button className={`${buttonClass} ${numberClass}`} onClick={inputDecimal}>
              ,
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={handleEquals}>
              =
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};