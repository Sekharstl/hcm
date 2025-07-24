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
  Button,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';
import { useRouter } from 'next/router';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import VisibilityIcon from '@mui/icons-material/Visibility';
import { useSelector, useDispatch } from 'react-redux';
import { fetchCandidatesThunk } from '../../store/slices/candidateSlice';
import type { RootState, AppDispatch } from '../../store/store';
import Pagination from '@mui/material/Pagination';

interface Candidate {
  candidateId: string;
  tenantId: string;
  organizationId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  dateOfBirth: string;
  gender: string;
  nationality: string;
}

const CandidateList: React.FC = () => {
  const router = useRouter();
  const dispatch = useDispatch<AppDispatch>();
  const { candidates, loading, error, totalPages, page, size } = useSelector((state: RootState) => state.candidates);
  const [menuAnchorEl, setMenuAnchorEl] = React.useState<null | HTMLElement>(null);
  const [selectedCandidateId, setSelectedCandidateId] = React.useState<string | null>(null);
  const [sortBy, setSortBy] = React.useState('id');
  const [sortDirection, setSortDirection] = React.useState<'ASC' | 'DESC'>('ASC');
  const [currentPage, setCurrentPage] = React.useState(0);

  useEffect(() => {
    dispatch(fetchCandidatesThunk({ page: currentPage, size, sortBy, sortDirection }));
  }, [dispatch, currentPage, size, sortBy, sortDirection]);

  const handleSort = (column: string) => {
    if (sortBy === column) {
      setSortDirection((prev) => (prev === 'ASC' ? 'DESC' : 'ASC'));
    } else {
      setSortBy(column);
      setSortDirection('ASC');
    }
  };

  const handlePageChange = (_: any, value: number) => {
    setCurrentPage(value - 1);
  };

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>, candidateId: string) => {
    setMenuAnchorEl(event.currentTarget);
    setSelectedCandidateId(candidateId);
  };

  const handleMenuClose = () => {
    setMenuAnchorEl(null);
    setSelectedCandidateId(null);
  };

  const handleEdit = () => {
    if (selectedCandidateId) {
      router.push({
        pathname: `/dashboard/candidates/${selectedCandidateId}`,
        query: { edit: '1' }
      });
      handleMenuClose();
    }
  };

  const handleDeleteMenu = () => {
    if (selectedCandidateId) {
      // handleDelete(selectedCandidateId); // Implement delete logic with redux if needed
      handleMenuClose();
    }
  };

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5" component="h1" sx={{ fontSize: '1rem', marginTop:'16px'}}>
          Candidate Management
        </Typography>
        <Button
          variant="contained"
          color="primary"
          sx={{ fontSize: '14px', height: '32px'}}
          onClick={() => router.push('/dashboard/candidates/new')}
        >
          Add Candidate
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table size="small" sx={{ '& .MuiTableCell-root': { fontSize: '0.75rem', py: 0.5 } }}>
          <TableHead>
            <TableRow>
              <TableCell onClick={() => handleSort('firstName')} sx={{ cursor: 'pointer' }}>
                Name {sortBy === 'firstName' && (sortDirection === 'ASC' ? '▲' : '▼')}
              </TableCell>
              <TableCell onClick={() => handleSort('email')} sx={{ cursor: 'pointer' }}>
                Email {sortBy === 'email' && (sortDirection === 'ASC' ? '▲' : '▼')}
              </TableCell>
              <TableCell onClick={() => handleSort('phone')} sx={{ cursor: 'pointer' }}>
                Phone {sortBy === 'phone' && (sortDirection === 'ASC' ? '▲' : '▼')}
              </TableCell>
              <TableCell onClick={() => handleSort('gender')} sx={{ cursor: 'pointer' }}>
                Gender {sortBy === 'gender' && (sortDirection === 'ASC' ? '▲' : '▼')}
              </TableCell>
              <TableCell onClick={() => handleSort('nationality')} sx={{ cursor: 'pointer' }}>
                Nationality {sortBy === 'nationality' && (sortDirection === 'ASC' ? '▲' : '▼')}
              </TableCell>
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
            {candidates.map((candidate) => (
              <TableRow key={candidate.candidateId}>
                <TableCell>{candidate.firstName} {candidate.lastName}</TableCell>
                <TableCell>{candidate.email}</TableCell>
                <TableCell>{candidate.phone}</TableCell>
                <TableCell>{candidate.gender}</TableCell>
                <TableCell>{candidate.nationality}</TableCell>
                <TableCell align="center" sx={{ width: 140, whiteSpace: 'nowrap' }}>
                  <IconButton
                    color="primary"
                    onClick={() => router.push(`/dashboard/candidates/${candidate.candidateId}`)}
                    sx={{ mr: 1, p: 0.5 }}
                    className="text-base"
                  >
                    <VisibilityIcon fontSize="inherit" />
                  </IconButton>
                  <IconButton
                    onClick={(e) => handleMenuOpen(e, candidate.candidateId)}
                    sx={{ p: 0.5 }}
                    className="text-base"
                  >
                    <MoreVertIcon fontSize="inherit" />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
        <Pagination
          count={totalPages}
          page={currentPage + 1}
          onChange={handlePageChange}
          color="primary"
          showFirstButton 
          showLastButton
          size="small"
        />
      </Box>

      <Menu
        anchorEl={menuAnchorEl}
        open={Boolean(menuAnchorEl)}
        onClose={handleMenuClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <MenuItem onClick={handleEdit}>Edit</MenuItem>
        <MenuItem onClick={handleDeleteMenu} sx={{ color: 'error.main' }}>Delete</MenuItem>
      </Menu>
    </Box>
  );
};

export default CandidateList; 