import Link from 'next/link';
import React from 'react';

interface SidebarProps {
  visibleModules: Array<{
    name: string;
    path: string;
    icon: React.ReactNode;
    groups: string[];
  }>;
  activeModule?: { name: string; path: string; groups: string[] };
  sidebarOpen: boolean;
}

const Sidebar: React.FC<SidebarProps> = ({ visibleModules, activeModule, sidebarOpen }) => (
  <div className={`fixed left-0 top-0 h-screen z-30 bg-white border-r border-gray-200 flex flex-col transition-all duration-200 ${sidebarOpen ? 'w-56' : 'w-16'} shadow-lg`}>
    <div className="h-16 flex-shrink-0" />
    <nav className="flex-1 flex flex-col pt-4 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-300">
      {visibleModules.map((mod) => (
        <Link key={mod.name} href={mod.path} legacyBehavior>
          <a className={`flex items-center ${sidebarOpen ? 'justify-start px-4' : 'justify-center'} py-3 mb-1 rounded-lg transition-colors duration-150 ${activeModule && activeModule.path === mod.path ? 'bg-purple-100 text-purple-700' : 'text-gray-500 hover:bg-gray-100'} group`}>
            <span className={`flex items-center justify-center ${sidebarOpen ? 'mr-3' : ''}`}>{mod.icon}</span>
            {sidebarOpen && <span className="font-medium text-sm truncate">{mod.name}</span>}
          </a>
        </Link>
      ))}
    </nav>
  </div>
);

export default Sidebar; 