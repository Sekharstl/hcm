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
  Chip,
  IconButton,
  Tooltip,
} from '@mui/material';
import { useRouter } from 'next/router';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store/store';
import { setJobs, deleteJob } from '../../store/slices/jobSlice';
import { Job } from '../../store/slices/jobSlice';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';

const JobList: React.FC = () => {
  const router = useRouter();
  const dispatch = useDispatch();
  const jobsState = useSelector((state: RootState) => state.jobs || { jobs: [], loading: false, error: null });
  const { jobs, loading, error } = jobsState;

  useEffect(() => {
    // TODO: Replace with actual API call
    const mockJobs: Job[] = [
      {
        id: '1',
        title: 'Senior Software Engineer',
        department: 'Engineering',
        jobType: 'Full-time',
        experienceLevel: 'Senior Level',
        location: 'San Francisco, CA',
        description: 'We are looking for a Senior Software Engineer to join our team...',
        requirements: '5+ years of experience in software development...',
        responsibilities: 'Design and implement new features...',
        salaryRange: '$120,000 - $150,000',
        postedDate: '2024-03-01',
        isActive: true,
      },
      {
        id: '2',
        title: 'Product Manager',
        department: 'Product',
        jobType: 'Full-time',
        experienceLevel: 'Mid Level',
        location: 'New York, NY',
        description: 'We are seeking a Product Manager to drive our product strategy...',
        requirements: '3+ years of product management experience...',
        responsibilities: 'Define product vision and strategy...',
        salaryRange: '$100,000 - $130,000',
        postedDate: '2024-03-05',
        isActive: true,
      },
    ];
    dispatch(setJobs(mockJobs));
  }, [dispatch]);

  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this job posting?')) {
      dispatch(deleteJob(id));
    }
  };

  if (loading) return <Typography>Loading...</Typography>;
  if (error) return <Typography color="error">{error}</Typography>;

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h5" component="h1">
          Job Postings
        </Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => router.push('/jobs/new')}
        >
          Create Job Posting
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Title</TableCell>
              <TableCell>Department</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Location</TableCell>
              <TableCell>Posted Date</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {jobs.map((job: Job) => (
              <TableRow key={job.id}>
                <TableCell>{job.title}</TableCell>
                <TableCell>{job.department}</TableCell>
                <TableCell>{job.jobType}</TableCell>
                <TableCell>{job.location}</TableCell>
                <TableCell>{new Date(job.postedDate).toLocaleDateString()}</TableCell>
                <TableCell>
                  <Chip
                    label={job.isActive ? 'Active' : 'Inactive'}
                    color={job.isActive ? 'success' : 'error'}
                    size="small"
                  />
                </TableCell>
                <TableCell align="right">
                  <Tooltip title="Edit">
                    <IconButton
                      onClick={() => router.push(`/jobs/${job.id}`)}
                      color="primary"
                      size="small"
                    >
                      <EditIcon />
                    </IconButton>
                  </Tooltip>
                  <Tooltip title="Delete">
                    <IconButton
                      onClick={() => handleDelete(job.id)}
                      color="error"
                      size="small"
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default JobList; 