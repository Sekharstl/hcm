import React, { useEffect, useState } from 'react';
import { Box, Paper, Typography, Button } from '@mui/material';
import { useRouter } from 'next/router';
import { fetchCandidateById } from '../../services/candidateService';

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

const CandidateDetails: React.FC = () => {
  const router = useRouter();
  let { id } = router.query;
  if (Array.isArray(id)) id = id[0];

  const [candidate, setCandidate] = useState<Candidate | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      setLoading(true);
      fetchCandidateById(id as string)
        .then((data) => {
          setCandidate(data);
          setError(null);
        })
        .catch(() => {
          setError('Candidate not found');
          setCandidate(null);
        })
        .finally(() => setLoading(false));
    }
  }, [id]);

  if (loading) {
    return <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}><Paper sx={{ p: 3 }}><Typography>Loading...</Typography></Paper></Box>;
  }
  if (error || !candidate) {
    return <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}><Paper sx={{ p: 3 }}><Typography>{error || 'Candidate not found'}</Typography></Paper></Box>;
  }

  return (
    <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}>
      <Paper sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <h1 className="text-base font-semibold text-gray-800">Candidate Details</h1>
          <button
            className="btn-primary text-xs font-semibold px-4 py-1.5"
            onClick={() => router.push({ pathname: router.pathname, query: { ...router.query, edit: '1' } })}
          >
            Edit
          </button>
        </Box>
        <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">First Name</div>
            <div className="text-xs">{candidate.firstName}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Last Name</div>
            <div className="text-xs">{candidate.lastName}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Email</div>
            <div className="text-xs">{candidate.email}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Phone</div>
            <div className="text-xs">{candidate.phone}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Address</div>
            <div className="text-xs">{candidate.address}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Date of Birth</div>
            <div className="text-xs">{candidate.dateOfBirth}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Gender</div>
            <div className="text-xs">{candidate.gender}</div>
          </Box>
          <Box>
            <div className="text-xs text-gray-500 font-semibold">Nationality</div>
            <div className="text-xs">{candidate.nationality}</div>
          </Box>
        </Box>
        <div className="w-full flex justify-center mt-8">
          <button
            className="px-4 py-1.5 text-xs font-semibold rounded border border-purple-400 text-purple-600 bg-white hover:bg-purple-50 transition"
            onClick={() => router.push('/dashboard/candidates')}
          >
            Cancel
          </button>
        </div>
      </Paper>
    </Box>
  );
};

export default CandidateDetails; 