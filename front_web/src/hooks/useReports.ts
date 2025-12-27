import { useQuery } from '@tanstack/react-query';
import { reportsService } from '@/services/reports.service';

export const useReports = () => {
  const { data: dashboardData, isLoading: isDashboardLoading } = useQuery({
    queryKey: ['dashboard'],
    queryFn: () => reportsService.getDashboard(),
    refetchInterval: 30000,
  });

  const useSalesReport = (dataInicio?: string, dataFim?: string) => {
    return useQuery({
      queryKey: ['sales-report', dataInicio, dataFim],
      queryFn: () => reportsService.getSales(dataInicio, dataFim),
      enabled: !!dataInicio && !!dataFim,
    });
  };

  const { data: productsReport, isLoading: isProductsLoading } = useQuery({
    queryKey: ['products-report'],
    queryFn: () => reportsService.getProducts(),
  });

  return {
    dashboardData,
    isDashboardLoading,
    productsReport,
    isProductsLoading,
    useSalesReport,
  };
};