import React, { useEffect, useState, Suspense } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Paper,
  IconButton,
  useTheme,
  LinearProgress,
  Tooltip,
  Grid,
  Button,
  Menu,
  MenuItem,
  ListItemIcon,
  ListItemText,
  Divider,
  Chip,
  Avatar,
  CardHeader,
  CardActions,
  useMediaQuery,
} from '@mui/material';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip as RechartsTooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line,
  TooltipProps,
} from 'recharts';
import {
  People as PeopleIcon,
  Work as WorkIcon,
  Event as EventIcon,
  AttachMoney as MoneyIcon,
  MoreVert as MoreVertIcon,
  TrendingUp as TrendingUpIcon,
  Business as BusinessIcon,
  Assignment as AssignmentIcon,
  Payment as PaymentIcon,
  Assessment as AssessmentIcon,
  Settings as SettingsIcon,
  Add as AddIcon,
  List as ListIcon,
  ArrowForward as ArrowForwardIcon,
  PostAdd as PostAddIcon,
  Person as PersonIcon,
  WorkOutline as WorkOutlineIcon,
  Description as DescriptionIcon,
  Store as StoreIcon,
  Task as TaskIcon,
  AccountBalance as AccountBalanceIcon,
  InsertChart as InsertChartIcon,
  Tune as TuneIcon,
  Dashboard as DashboardIcon,
  Update as UpdateIcon,
} from '@mui/icons-material';
import { useRouter } from 'next/router';
import Loader from './Loader';

interface Module {
  id: string;
  title: string;
  description: string;
  icon: React.ReactNode;
  path: string;
}

interface ChartData {
  name: string;
  value: number;
}

interface CustomTooltipProps extends TooltipProps<number, string> {
  payload?: Array<{
    name: string;
    value: number;
  }>;
}

const CustomTooltip: React.FC<CustomTooltipProps> = ({ payload }) => {
  if (payload && payload[0]) {
    return (
      <Box sx={{ p: 1, backgroundColor: 'white', borderRadius: 1, boxShadow: 1 }}>
        <Typography variant="body2">
          {payload[0].name}: {payload[0].value}
        </Typography>
      </Box>
    );
  }
  return null;
};

// DashboardCard component for clarity and maintainability
type DashboardCardProps = {
  icon: React.ReactNode;
  title: string;
  description: string;
  buttonText: string;
  onClick: () => void;
};

const DashboardCard: React.FC<DashboardCardProps> = ({ icon, title, description, buttonText, onClick }) => (
  <div className="flex flex-col items-center justify-start bg-white rounded-2xl border border-gray-200 shadow-sm p-2 min-h-[160px] w-full max-w-sm mx-auto transition-shadow hover:shadow-lg">
    <div className="flex items-center justify-center mb-2 w-full">
      {icon}
    </div>
    <div className="text-center w-full">
      <div className="font-semibold text-[1rem] mb-1 text-gray-900">{title}</div>
      <div className="text-gray-500 text-[0.8rem] mb-4 w-full line-clamp-2" style={{ display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden', textOverflow: 'ellipsis' }} title={description}>{description}</div>
    </div>
    <button
      onClick={onClick}
      className="btn-primary w-full mt-auto py-1 h-8 rounded-xl font-medium text-sm leading-5 flex items-center justify-center gap-2 focus:outline-none focus:ring-2 focus:ring-primary pl-4 pr-4 min-w-44"
    >
      {buttonText}
      <svg className="ml-2 w-5 h-5" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" d="M17 8l4 4m0 0l-4 4m4-4H3"></path></svg>
    </button>
  </div>
);

const Dashboard: React.FC = () => {
  const theme = useTheme();
  const router = useRouter();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const [employeeAnchorEl, setEmployeeAnchorEl] = React.useState<null | HTMLElement>(null);
  const [vendorAnchorEl, setVendorAnchorEl] = React.useState<null | HTMLElement>(null);
  const [lastUpdated, setLastUpdated] = useState('');

  useEffect(() => {
    setLastUpdated(new Date().toLocaleDateString());
  }, []);

  const handleEmployeeClick = (event: React.MouseEvent<HTMLElement>) => {
    setEmployeeAnchorEl(event.currentTarget);
  };

  const handleVendorClick = (event: React.MouseEvent<HTMLElement>) => {
    setVendorAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setEmployeeAnchorEl(null);
    setVendorAnchorEl(null);
  };

  const handleNavigation = (path: string) => {
    router.push(path);
    handleClose();
  };

  const modules: Module[] = [
    // {
    //   id: 'employees',
    //   title: 'Employees',
    //   description: 'Manage employee information, attendance, and performance',
    //   icon: <PeopleIcon fontSize="large" color="primary" />,
    //   path: '/dashboard/employees',
    // },
    {
      id: 'jobs',
      title: 'Jobs',
      description: 'Create and manage job postings',
      icon: <WorkOutlineIcon fontSize="large" color="primary" />,
      path: '/jobs',
    },
    {
      id: 'job-postings',
      title: 'Job Postings',
      description: 'View and manage active job postings',
      icon: <PostAddIcon fontSize="large" color="primary" />,
      path: '/jobs',
    },
    {
      id: 'vendors',
      title: 'Vendors',
      description: 'Manage vendor relationships and contracts',
      icon: <StoreIcon fontSize="large" color="primary" />,
      path: '/dashboard/vendors',
    },
    {
      id: 'tasks',
      title: 'Tasks',
      description: 'Assign and track employee tasks and projects',
      icon: <TaskIcon fontSize="large" color="primary" />,
      path: '/tasks',
    },
    // {
    //   id: 'payroll',
    //   title: 'Payroll',
    //   description: 'Process payroll and manage employee compensation',
    //   icon: <PaymentIcon fontSize="large" color="primary" />,
    //   path: '/payroll',
    // },
    {
      id: 'reports',
      title: 'Reports',
      description: 'Generate and view various HR reports',
      icon: <AssessmentIcon fontSize="large" color="primary" />,
      path: '/reports',
    },
    {
      id: 'settings',
      title: 'Settings',
      description: 'Configure system settings and preferences',
      icon: <SettingsIcon fontSize="large" color="primary" />,
      path: '/settings',
    },
  ];

  const departmentData: ChartData[] = [
    { name: 'Engineering', value: 30 },
    { name: 'Marketing', value: 20 },
    { name: 'Sales', value: 25 },
    { name: 'HR', value: 15 },
    { name: 'Finance', value: 10 },
  ];

  const leaveData: ChartData[] = [
    { name: 'Approved', value: 45 },
    { name: 'Pending', value: 15 },
    { name: 'Rejected', value: 5 },
  ];

  const revenueData: ChartData[] = [
    { name: 'Jan', value: 4000 },
    { name: 'Feb', value: 3000 },
    { name: 'Mar', value: 5000 },
    { name: 'Apr', value: 4500 },
    { name: 'May', value: 6000 },
    { name: 'Jun', value: 5500 },
  ];

  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

  return (
    <Suspense fallback={<Loader />}>
      <div className="w-full min-h-screen bg-gray-50 p-2 md:p-4 pt-0">
        <Paper 
          elevation={0} 
          className="flex items-center justify-between bg-white rounded-lg border border-gray-200 shadow-none mb-4 px-4 py-3 min-h-[56px]"
        >
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
            <DashboardIcon sx={{ fontSize: 24, mr: 1, color: theme.palette.primary.main }} />
            <Box>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 0.2, lineHeight: 1, fontSize: '1.1rem', color: theme.palette.primary.main }}>
                Dashboard
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.7, fontWeight: 400, fontSize: '0.8rem', color: theme.palette.text.secondary }}>
                Welcome to your HR management dashboard
              </Typography>
            </Box>
          </Box>
          <Chip 
            icon={<UpdateIcon sx={{ fontSize: 15 }} />} 
            label={`Last updated: ${lastUpdated}`} 
            size="small"
            className="bg-gray-100 text-gray-500 border border-gray-200 text-xs h-6 px-2 font-normal"
          />
        </Paper>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 mb-8">
          {modules.map((module) => (
            <DashboardCard
              key={module.id}
              icon={module.icon}
              title={module.title}
              description={module.description}
              buttonText={'Manage'}
              onClick={
                module.id === 'employees'
                  ? () => router.push(module.path)
                  : module.id === 'vendors'
                  ? () => router.push(module.path)
                  : () => router.push('/')
              }
            />
          ))}
        </div>

        {/* Charts */}
        <Box sx={{ 
          display: 'flex', 
          flexDirection: { xs: 'column', md: 'row' }, 
          gap: 3, 
          mb: 4 
        }}>
          <Box sx={{ flex: 1 }}>
            <Paper 
              sx={{ 
                p: 3,
                height: 400,
                borderRadius: 2,
                boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.10)',
                background: 'rgba(255,255,255,0.7)',
                backdropFilter: 'blur(6px)',
                overflow: 'hidden',
              }}
            >
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Department Distribution
                </Typography>
                <Chip 
                  label="Employee Data" 
                  size="small" 
                  sx={{ 
                    bgcolor: theme.palette.primary.light,
                    color: theme.palette.primary.main,
                  }} 
                />
              </Box>
              <Divider sx={{ mb: 2 }} />
              <Box sx={{ height: 'calc(100% - 80px)' }}>
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={departmentData}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      label={({ name, percent }) => percent !== undefined ? `${name} ${(percent * 100).toFixed(0)}%` : name}
                      outerRadius={80}
                      fill="#8884d8"
                      dataKey="value"
                    >
                      {departmentData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
                    <RechartsTooltip />
                  </PieChart>
                </ResponsiveContainer>
              </Box>
            </Paper>
          </Box>

          <Box sx={{ flex: 1 }}>
            <Paper 
              sx={{ 
                p: 3,
                height: 400,
                borderRadius: 2,
                boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.10)',
                background: 'rgba(255,255,255,0.7)',
                backdropFilter: 'blur(6px)',
                overflow: 'hidden',
              }}
            >
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Leave Status
                </Typography>
                <Chip 
                  label="Attendance Data" 
                  size="small" 
                  sx={{ 
                    bgcolor: theme.palette.secondary.light,
                    color: theme.palette.secondary.main,
                  }} 
                />
              </Box>
              <Divider sx={{ mb: 2 }} />
              <Box sx={{ height: 'calc(100% - 80px)' }}>
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={leaveData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <RechartsTooltip />
                    <Legend />
                    <Bar dataKey="value" fill={theme.palette.primary.main} />
                  </BarChart>
                </ResponsiveContainer>
              </Box>
            </Paper>
          </Box>
        </Box>

        {/* Revenue Chart */}
        <Paper 
          sx={{ 
            p: 3, 
            height: '400px', 
            borderRadius: 2,
            boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.10)',
            background: 'rgba(255,255,255,0.7)',
            backdropFilter: 'blur(6px)',
            overflow: 'hidden',
          }}
        >
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              Revenue Trend
            </Typography>
            <Chip 
              label="Financial Data" 
              size="small" 
              sx={{ 
                bgcolor: theme.palette.success.light,
                color: theme.palette.success.main,
              }} 
            />
          </Box>
          <Divider sx={{ mb: 2 }} />
          <Box sx={{ height: 'calc(100% - 80px)' }}>
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={revenueData}>
                <CartesianGrid strokeDasharray="3 3" stroke={theme.palette.divider} />
                <XAxis 
                  dataKey="name" 
                  tick={{ fill: theme.palette.text.primary }}
                />
                <YAxis 
                  tick={{ fill: theme.palette.text.primary }}
                />
                <RechartsTooltip 
                  contentStyle={{ 
                    backgroundColor: theme.palette.background.paper,
                    border: `1px solid ${theme.palette.divider}`,
                    borderRadius: 4,
                  }}
                />
                <Legend />
                <Line 
                  type="monotone" 
                  dataKey="value" 
                  stroke={theme.palette.primary.main}
                  strokeWidth={2}
                  dot={{ r: 4 }}
                  activeDot={{ r: 6 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </Box>
        </Paper>
      </div>
    </Suspense>
  );
};

export default Dashboard; 