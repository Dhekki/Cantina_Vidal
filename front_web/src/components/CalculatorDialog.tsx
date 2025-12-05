import { Calculator } from 'lucide-react';
import { useState } from 'react';

import {
  Dialog,
  DialogTitle,
  DialogHeader,
  DialogTrigger,
  DialogContent,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';

export const CalculatorDialog = () => {
  const [display, setDisplay] = useState('0');
  const [previousValue, setPreviousValue] = useState<number | null>(null);
  const [operator, setOperator] = useState<string | null>(null);
  const [waitingForOperand, setWaitingForOperand] = useState(false);

  const inputDigit = (digit: string) => {
    if(waitingForOperand) {
      setDisplay(digit);
      setWaitingForOperand(false);
    } else {
      setDisplay(display === '0' ? digit : display + digit);
    }
  };

  const inputDecimal = () => {
    if(waitingForOperand) {
      setDisplay('0.');
      setWaitingForOperand(false);
      return;
    }
    if(!display.includes('.')) {
      setDisplay(display + '.');
    }
  };

  const clear = () => {
    setDisplay('0');
    setPreviousValue(null);
    setOperator(null);
    setWaitingForOperand(false);
  };

  const performOperation = (nextOperator: string) => {
    const inputValue = parseFloat(display);

    if(previousValue === null) {
      setPreviousValue(inputValue);
    } else if(operator) {
      const result = calculate(previousValue, inputValue, operator);
      setDisplay(String(result));
      setPreviousValue(result);
    }

    setWaitingForOperand(true);
    setOperator(nextOperator);
  };

  const calculate = (a: number, b: number, op: string): number => {
    switch (op) {
      case '+': return a + b;
      case '-': return a - b;
      case '×': return a * b;
      case '÷': return b !== 0 ? a / b : 0;
      default : return b;
    }
  };

  const handleEquals = () => {
    if(operator && previousValue !== null) {
      const inputValue = parseFloat(display);
      const result = calculate(previousValue, inputValue, operator);

      setDisplay(String(result));

      setOperator(null);
      setPreviousValue(null);
      setWaitingForOperand(true);
    }
  };

  const handlePercent = () => {
    const value = parseFloat(display);
    setDisplay(String(value / 100));
  };

  const handlePlusMinus = () => {
    const value = parseFloat(display);
    setDisplay(String(value * -1));
  };

  const buttonClass   = "h-14 text-lg font-medium";
  const operatorClass = "bg-primary text-primary-foreground hover:bg-primary/90";
  const functionClass = "bg-muted hover:bg-muted/80";

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="ghost" size="icon" className="h-9 w-9">
          <Calculator className="h-5 w-5" />
        </Button>
      </DialogTrigger>
      <DialogContent className="max-w-xs p-4">
        <DialogHeader>
          <DialogTitle className="sr-only">Calculadora</DialogTitle>
        </DialogHeader>
        
        <div className="space-y-2">
          {/* Display */}
          <div className="bg-muted rounded-lg p-4 text-right">
            <span className="text-3xl font-mono truncate block">
              {display}
            </span>
          </div>

          {/* Buttons */}
          <div className="grid grid-cols-4 gap-2">
            <Button className={`${buttonClass} ${functionClass}`} onClick={clear}>
              AC
            </Button>
            <Button className={`${buttonClass} ${functionClass}`} onClick={handlePlusMinus}>
              ±
            </Button>
            <Button className={`${buttonClass} ${functionClass}`} onClick={handlePercent}>
              %
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('÷')}>
              ÷
            </Button>

            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('7')}>
              7
            </Button>
            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('8')}>
              8
            </Button>
            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('9')}>
              9
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('×')}>
              ×
            </Button>

            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('4')}>
              4
            </Button>
            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('5')}>
              5
            </Button>
            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('6')}>
              6
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('-')}>
              −
            </Button>

            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('1')}>
              1
            </Button>
            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('2')}>
              2
            </Button>
            <Button className={buttonClass} variant="outline" onClick={() => inputDigit('3')}>
              3
            </Button>
            <Button className={`${buttonClass} ${operatorClass}`} onClick={() => performOperation('+')}>
              +
            </Button>

            <Button className={`${buttonClass} col-span-2`} variant="outline" onClick={() => inputDigit('0')}>
              0
            </Button>
            <Button className={buttonClass} variant="outline" onClick={inputDecimal}>
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
