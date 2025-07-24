import React, { useEffect, useState } from 'react';
import { Box, Paper, TextField, Button, Typography, MenuItem } from '@mui/material';
import { useRouter } from 'next/router';
import { useDispatch, useSelector } from 'react-redux';
import { addVendor, updateVendor } from '../../store/slices/vendorSlice';
import type { RootState, AppDispatch } from '../../store/store';

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

const VendorForm: React.FC = () => {
  const router = useRouter();
  let { id, edit } = router.query;
  if (Array.isArray(id)) id = id[0];
  const dispatch = useDispatch<AppDispatch>();
  const vendors = useSelector((state: RootState) => state.vendors.vendors);
  const [formData, setFormData] = useState<Partial<VendorDTO>>({
    vendorName: '',
    contactName: '',
    contactEmail: '',
    contactPhone: '',
    address: '',
    statusId: 1,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id && edit === '1') {
      const vendor = vendors.find((v) => v.vendorId === id);
      if (vendor) {
        setFormData(vendor);
      }
    }
  }, [id, edit, vendors]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      if (id && edit === '1') {
        dispatch(updateVendor({ ...formData, vendorId: id }));
      } else {
        const newVendor: VendorDTO = {
          ...formData,
          vendorId: Date.now().toString(),
          tenantId: 'tenant-uuid',
          organizationId: 'org-uuid',
          createdAt: new Date().toISOString(),
          createdBy: 'user-uuid',
          updatedAt: new Date().toISOString(),
          updatedBy: 'user-uuid',
        } as VendorDTO;
        dispatch(addVendor(newVendor));
      }
      setTimeout(() => router.push('/dashboard/vendors'), 800);
    } catch (err) {
      setError('Failed to save vendor');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}><Paper sx={{ p: 3 }}><Typography>Loading...</Typography></Paper></Box>;
  }
  if (error) {
    return <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}><Paper sx={{ p: 3 }}><Typography>{error}</Typography></Paper></Box>;
  }

  return (
    <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}>
      <Paper sx={{ p: 3 }}>
        <Typography variant="h5" component="h1" gutterBottom sx={{ marginBottom: '1.35rem' }}>
          <span className="text-base font-semibold text-gray-800">
            {id && edit === '1' ? 'Edit Vendor' : 'Add Vendor'}
          </span>
        </Typography>
        <form onSubmit={handleSubmit}>
          <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
            <TextField
              fullWidth
              label="Vendor Name"
              name="vendorName"
              value={formData.vendorName}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Contact Name"
              name="contactName"
              value={formData.contactName}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Contact Email"
              name="contactEmail"
              type="email"
              value={formData.contactEmail}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Contact Phone"
              name="contactPhone"
              value={formData.contactPhone}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Address"
              name="address"
              value={formData.address}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              select
              label="Status"
              name="statusId"
              value={formData.statusId}
              onChange={handleChange}
              required
            >
              <MenuItem value={1}>Active</MenuItem>
              <MenuItem value={0}>Inactive</MenuItem>
            </TextField>
          </Box>
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end', mt: 3 }}>
            <Button
              type="button"
              variant="outlined"
              color="secondary"
              onClick={() => router.push('/dashboard/vendors')}
              sx={{ borderWidth: 2, fontSize: '0.75rem', fontWeight: 600, px: 2.5, py: 1.2 }}
            >
              Cancel
            </Button>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={loading}
              sx={{ fontSize: '0.75rem', fontWeight: 600, px: 2.5, py: 1.2 }}
            >
              {loading ? 'Saving...' : id && edit === '1' ? 'Update' : 'Create'} Vendor
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default VendorForm; 