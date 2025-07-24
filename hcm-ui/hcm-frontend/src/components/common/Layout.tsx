import { ReactNode, useState, useRef, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { signOut } from 'next-auth/react';
import { useSession } from 'next-auth/react';
import BusinessIcon from '@mui/icons-material/Business';
import WorkOutlineOutlinedIcon from '@mui/icons-material/WorkOutlineOutlined';
import BadgeIcon from '@mui/icons-material/Badge';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import PeopleIcon from '@mui/icons-material/People';
import WorkIcon from '@mui/icons-material/Work';
import DescriptionIcon from '@mui/icons-material/Description';
import StoreIcon from '@mui/icons-material/Store';
import TaskIcon from '@mui/icons-material/Task';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import InsertChartIcon from '@mui/icons-material/InsertChart';
import TuneIcon from '@mui/icons-material/Tune';
import AssessmentIcon from '@mui/icons-material/Assessment';
import EventIcon from '@mui/icons-material/Event';
import { AppBar, Toolbar, IconButton, Avatar, Drawer, List, ListItemIcon, ListItemText, Divider, Typography, Box, Tooltip, useTheme, useMediaQuery, ListItemButton, alpha } from '@mui/material';
import Loader from './Loader';
import Sidebar from './Sidebar';
import Image from 'next/image';
import { useLoader } from './LoaderContext';

const allModules = [
  { name: 'Dashboard', path: '/dashboard', icon: <BusinessIcon />, groups: ['Admin', 'Employee', 'Vendor'] },
  { name: 'Candidate', path: '/dashboard/candidates', icon: <PeopleIcon />, groups: ['Admin', 'Vendor'] },
  // { name: 'Employee', path: '/dashboard/employees', icon: <BadgeIcon />, groups: ['Admin'] },
  { name: 'Vendor', path: '/dashboard/vendors', icon: <StoreIcon />, groups: ['Admin'] },
  { name: 'Jobs', path: '/dashboard/jobs', icon: <WorkIcon />, groups: ['Admin', 'Employee'] },
  { name: 'Requisitions', path: '/dashboard/requisitions', icon: <AccountBalanceIcon />, groups: ['Admin', 'Employee'] },
  // { name: 'Performance', path: '/dashboard/performance', icon: <AssessmentIcon />, groups: ['Admin', 'Employee'] },
  { name: 'Reports', path: '/dashboard/reports', icon: <InsertChartIcon />, groups: ['Admin', 'Employee'] },
  { name: 'Settings', path: '/dashboard/settings', icon: <TuneIcon />, groups: ['Admin', 'Employee'] },
];

interface LayoutProps {
  children: ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  const { data: session } = useSession();
  const router = useRouter();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const userGroups = (session?.user as any)?.groups || [];
  const userRole = userGroups.find((g: string) => g !== 'Everyone');
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [profileDropdownOpen, setProfileDropdownOpen] = useState(false);
  const profileRef = useRef<HTMLDivElement>(null);
  const { loading } = useLoader();

  useEffect(() => {
    setSidebarOpen(!isMobile);
  }, [isMobile]);

  // Only show modules the user has access to
  const visibleModules = allModules
    .filter((mod) => mod.groups.some((group) => userGroups.includes(group)));

  // Determine the active module by the longest matching path
  const activeModule = visibleModules.reduce<{ name: string; path: string; groups: string[] } | undefined>((prev, curr) => {
    if (router.pathname.startsWith(curr.path) && (!prev || curr.path.length > prev.path.length)) {
      return curr;
    }
    return prev;
  }, undefined);

  const drawerWidth = sidebarOpen ? 220 : 64;

  // Close dropdown on click outside
  useEffect(() => {
    if (!profileDropdownOpen) return;
    function handleClickOutside(event: MouseEvent) {
      if (profileRef.current && !profileRef.current.contains(event.target as Node)) {
        setProfileDropdownOpen(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [profileDropdownOpen]);

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      {loading && <Loader />}
      {/* AppBar */}
      <AppBar position="fixed" color="inherit" elevation={0} sx={{ zIndex: theme.zIndex.drawer + 1, boxShadow: '0 2px 8px 0 rgba(31, 38, 135, 0.04)' }}>
        <Toolbar className="min-h-14">
          <IconButton
            color="primary"
            edge="start"
            onClick={() => setSidebarOpen((open) => !open)}
            sx={{ mr: 2 }}
          >
            {sidebarOpen ? <ChevronLeftIcon /> : <MenuIcon />}
          </IconButton>
          {/* Placeholder for company brand image */}
          <Box sx={{ width: 170, height: 56, overflow: 'hidden', display: 'flex', alignItems: 'center', justifyContent: 'center', mr: 2, bgcolor: 'white', pl: 1, pr: 1 }}>
            <Image src="/STL_logo.png" alt="Company Brand" width={70} height={56} style={{ objectFit: 'contain', transform: 'scale(1.2)' }} />
            <Divider orientation="vertical" flexItem sx={{ mx: 2, height: 40, borderColor: '#ccc', marginTop: '8px' }} />
            <Image src="/logo5.png" alt="Partner Brand" width={48} height={48} style={{ objectFit: 'contain', transform: 'scale(1.5)' }} />
          </Box>
          <Box sx={{ flexGrow: 1, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          {/* <Image src="/logo5.png" alt="Company Brand" width={85} height={85} style={{ objectFit: 'cover', transform: 'scale(1.2)' }} /> */}
            {/* <Typography variant="h5" color="primary" sx={{ fontWeight: 700 }}>
              eZHire - Easy Hiring Platform
            </Typography> */}
          </Box>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, position: 'relative' }}>
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mr: 1 }}>
              <span className="text-xs font-medium text-gray-700 text-center">{session?.user?.name}</span>
              {userRole && (
                <span className="text-[10px] text-gray-400 text-center">{userRole}</span>
              )}
            </Box>
            <div ref={profileRef} className="relative">
              <button
                onClick={() => setProfileDropdownOpen((open) => !open)}
                className="focus:outline-none"
                aria-label="Open profile menu"
              >
                <Avatar sx={{ bgcolor: theme.palette.primary.main, width: 32, height: 32 }}>
                  {session?.user?.name?.[0]?.toUpperCase() || '?'}
                </Avatar>
              </button>
              {profileDropdownOpen && (
                <div className="absolute right-0 mt-2 w-40 bg-white border border-gray-200 rounded-lg shadow-lg z-50">
                  <button
                    onClick={() => signOut()}
                    className="w-full text-left px-4 py-2 text-red-600 hover:bg-gray-100 rounded-lg"
                  >
                    Sign out
                  </button>
                </div>
              )}
            </div>
          </Box>
        </Toolbar>
      </AppBar>
      {/* Sidebar */}
      <Sidebar visibleModules={visibleModules} activeModule={activeModule} sidebarOpen={sidebarOpen} />
      {/* Main Content */}
      <main className={`flex-1 min-h-screen transition-all duration-200 p-2 md:p-6 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-300 pt-16 ${sidebarOpen ? 'md:ml-56' : 'md:ml-16'}`}>
        <div className="h-12 flex-shrink-0" />
        {children}
      </main>
    </Box>
  );
}