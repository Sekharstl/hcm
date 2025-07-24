import React, { useEffect } from 'react';
import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  IconButton,
  Button
} from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import EditIcon from '@mui/icons-material/Edit';
import { useRouter } from 'next/router';
import { useSelector, useDispatch } from 'react-redux';
import { fetchVendorsThunk } from '../../store/slices/vendorSlice';
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

const VendorList: React.FC = () => {
  const router = useRouter();
  const dispatch = useDispatch<AppDispatch>();
  const { vendors, loading, error } = useSelector((state: RootState) => state.vendors);

  useEffect(() => {
    dispatch(fetchVendorsThunk());
  }, [dispatch]);

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5" component="h1" sx={{ fontSize: '1rem', marginTop:'16px'}}>
          Vendor Management
        </Typography>
        <Button
          variant="contained"
          color="primary"
          sx={{ fontSize: '14px', height: '32px'}}
          onClick={() => router.push('/dashboard/vendors/new')}
        >
          Add Vendor
        </Button>
      </Box>
      <TableContainer component={Paper}>
        <Table size="small" sx={{ '& .MuiTableCell-root': { fontSize: '0.75rem', py: 0.5 } }}>
          <TableHead>
            <TableRow>
              <TableCell>Vendor Name</TableCell>
              <TableCell>Contact Name</TableCell>
              <TableCell>Contact Email</TableCell>
              <TableCell>Contact Phone</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="center" sx={{ width: 140, whiteSpace: 'nowrap' }}>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading && (
              <TableRow>
                <TableCell colSpan={6} align="center">Loading...</TableCell>
              </TableRow>
            )}
            {error && !loading && (
              <TableRow>
                <TableCell colSpan={6} align="center" style={{ color: 'red' }}>{error}</TableCell>
              </TableRow>
            )}
            {vendors.map((vendor: VendorDTO) => (
              <TableRow key={vendor.vendorId}>
                <TableCell>{vendor.vendorName}</TableCell>
                <TableCell>{vendor.contactName}</TableCell>
                <TableCell>{vendor.contactEmail}</TableCell>
                <TableCell>{vendor.contactPhone}</TableCell>
                <TableCell>{vendor.statusId === 1 ? 'Active' : 'Inactive'}</TableCell>
                <TableCell align="center" sx={{ width: 140, whiteSpace: 'nowrap' }}>
                  <IconButton
                    color="primary"
                    onClick={() => router.push(`/dashboard/vendors/${vendor.vendorId}`)}
                    sx={{ mr: 1, p: 0.5 }}
                    className="text-base"
                  >
                    <VisibilityIcon fontSize="inherit" />
                  </IconButton>
                  <IconButton
                    onClick={() => router.push({ pathname: `/dashboard/vendors/${vendor.vendorId}`, query: { edit: '1' } })}
                    sx={{ p: 0.5 }}
                    className="text-base"
                  >
                    <EditIcon fontSize="inherit" />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default VendorList; 