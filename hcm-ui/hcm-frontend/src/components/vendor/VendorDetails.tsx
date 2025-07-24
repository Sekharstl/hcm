import React, { useEffect, useState } from 'react';
import { Box, Paper } from '@mui/material';
import { useRouter } from 'next/router';
import { fetchVendorById } from '../../services/vendorService';

interface VendorDTO {
  vendorId: string;
  tenantId: string;
  organizationId: string;
  vendorName: string;
  contactName: string;
  contactEmail: string;
  contactPhone: string;
  address: string;
  statusId: number;
  createdAt: string;
  createdBy: string;
  updatedAt: string;
  updatedBy: string;
}

const VendorDetails: React.FC = () => {
  const router = useRouter();
  let { id } = router.query;
  if (Array.isArray(id)) id = id[0];

  const [vendor, setVendor] = useState<VendorDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      setLoading(true);
      fetchVendorById(id as string)
        .then((data) => {
          setVendor(data);
          setError(null);
        })
        .catch(() => {
          setError('Vendor not found');
          setVendor(null);
        })
        .finally(() => setLoading(false));
    }
  }, [id]);

  if (loading) {
    return <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}><Paper sx={{ p: 3 }}>Loading...</Paper></Box>;
  }
  if (error || !vendor) {
    return <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}><Paper sx={{ p: 3 }}>{error || 'Vendor not found'}</Paper></Box>;
  }

  return (
    <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}>
      <Paper sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <h1 className="text-base font-semibold text-gray-800">Vendor Details</h1>
          <button
            className="btn-primary text-xs font-semibold px-4 py-1.5"
            onClick={() => router.push({ pathname: router.pathname, query: { ...router.query, edit: '1' } })}
          >
            Edit
          </button>
        </Box>
        <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Vendor Name</div>
            <div className="text-xs">{vendor.vendorName}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Contact Name</div>
            <div className="text-xs">{vendor.contactName}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Contact Email</div>
            <div className="text-xs">{vendor.contactEmail}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Contact Phone</div>
            <div className="text-xs">{vendor.contactPhone}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Address</div>
            <div className="text-xs">{vendor.address}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Status</div>
            <div className="text-xs">{vendor.statusId === 1 ? 'Active' : 'Inactive'}</div>
          </Box>
        </Box>
        <div className="w-full flex justify-center mt-8">
          <button
            className="px-4 py-1.5 text-xs font-semibold rounded border-2 border-purple-500 text-purple-700 bg-white hover:bg-purple-50 transition"
            onClick={() => router.push('/dashboard/vendors')}
          >
            Cancel
          </button>
        </div>
      </Paper>
    </Box>
  );
};

export default VendorDetails; 