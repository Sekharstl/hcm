import React, { useEffect, useState } from 'react';
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  MenuItem,
  Snackbar,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  IconButton,
  Grid,
} from '@mui/material';
import MuiAlert from '@mui/material/Alert';
import { useRouter } from 'next/router';
import { fetchCandidateById, createCandidate, updateCandidate } from '../../services/candidateService';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import DeleteIcon from '@mui/icons-material/Delete';
import CandidateStepperForm from './CandidateStepperForm';

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

const genders = [
  'Male',
  'Female',
  'Other',
];

const CandidateForm: React.FC = () => {
  const router = useRouter();
  let { id } = router.query;
  if (Array.isArray(id)) id = id[0];

  const [formData, setFormData] = useState<Partial<Candidate>>({
    candidateId: '123',
    tenantId: '123',
    organizationId: '123',
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    address: '',
    dateOfBirth: '',
    gender: '',
    nationality: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [educations, setEducations] = useState([
    {
      educationId: undefined,
      institution: '',
      degree: '',
      fieldOfStudy: '',
      startDate: '',
      endDate: '',
      grade: '',
      notes: '',
      description: '',
      institutionName: '',
    },
  ]);

  useEffect(() => {
    if (id && id !== 'new') {
      setLoading(true);
      fetchCandidateById(id as string)
        .then((data) => {
          setFormData(data);
          setError(null);
        })
        .catch(() => {
          setError('Candidate not found');
        })
        .finally(() => setLoading(false));
    } else {
      setFormData({
        candidateId: '',
        tenantId: '',
        organizationId: '',
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        address: '',
        dateOfBirth: '',
        gender: '',
        nationality: '',
      });
    }
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleEducationChange = (idx: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setEducations((prev) => prev.map((edu, i) => i === idx ? { ...edu, [name]: value } : edu));
  };

  const addEducation = () => {
    setEducations((prev) => [
      ...prev,
      {
        educationId: undefined,
        institution: '',
        degree: '',
        fieldOfStudy: '',
        startDate: '',
        endDate: '',
        grade: '',
        notes: '',
        description: '',
        institutionName: '',
      },
    ]);
  };

  const removeEducation = (idx: number) => {
    setEducations((prev) => prev.filter((_, i) => i !== idx));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      if (id && id !== 'new') {
        await updateCandidate(id as string, formData);
        setSuccess('Candidate updated successfully!');
      } else {
        debugger
        await createCandidate(formData);
        setSuccess('Candidate created successfully!');
      }
      setTimeout(() => router.push('/dashboard/candidates'), 1200);
    } catch (err) {
      setError('Failed to save candidate');
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
            {id && id !== 'new' ? 'Edit Candidate' : 'Add Candidate'}
          </span>
        </Typography>
        <form onSubmit={handleSubmit}>
          <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
            <TextField
              fullWidth
              label="First Name"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Last Name"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Phone"
              name="phone"
              value={formData.phone}
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
              label="Date of Birth"
              name="dateOfBirth"
              type="date"
              value={formData.dateOfBirth}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
              required
            />
            <TextField
              fullWidth
              select
              label="Gender"
              name="gender"
              value={formData.gender}
              onChange={handleChange}
              required
            >
              {genders.map((gender) => (
                <MenuItem key={gender} value={gender}>
                  {gender}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              fullWidth
              label="Nationality"
              name="nationality"
              value={formData.nationality}
              onChange={handleChange}
              required
            />
            {/* Hidden fields for IDs */}
            <input type="hidden" name="candidateId" value={formData.candidateId} />
            <input type="hidden" name="tenantId" value={formData.tenantId} />
            <input type="hidden" name="organizationId" value={formData.organizationId} />
            <Box sx={{ gridColumn: '1 / -1', display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
              <Button
                type="button"
                variant="outlined"
                className="px-4 py-1.5 text-xs font-semibold rounded border-2 border-purple-500 text-purple-700 bg-white hover:bg-purple-50 transition"
                onClick={() => router.push('/dashboard/candidates')}
              >
                Cancel
              </Button>
              <Button type="submit" variant="contained" color="primary" disabled={loading}>
                <span className="text-xs font-semibold">
                  {loading ? 'Saving...' : id && id !== 'new' ? 'Update' : 'Create'} Candidate
                </span>
              </Button>
            </Box>
          </Box>
        </form>
        {/* Integrate the stepper for additional details */}
        {formData.candidateId && (
          <CandidateStepperForm candidateId={formData.candidateId} />
        )}
      </Paper>
      <Snackbar open={!!success} autoHideDuration={1200} onClose={() => setSuccess(null)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <MuiAlert onClose={() => setSuccess(null)} severity="success" sx={{ width: '100%' }} elevation={6} variant="filled">
          {success}
        </MuiAlert>
      </Snackbar>
    </Box>
  );
};

export default CandidateForm; 