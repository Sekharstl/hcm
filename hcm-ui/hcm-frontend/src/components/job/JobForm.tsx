import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  MenuItem,
  FormControlLabel,
  Switch,
} from '@mui/material';
import { useRouter } from 'next/router';
import { useDispatch, useSelector } from 'react-redux';
import { addJob, updateJob } from '../../store/slices/jobSlice';
import { Job } from '../../store/slices/jobSlice';
import { RootState } from '../../store/store';

const jobTypes = ['Full-time', 'Part-time', 'Contract', 'Internship'] as const;
const experienceLevels = ['Entry Level', 'Mid Level', 'Senior Level', 'Lead'] as const;
const departments = [
  'Engineering',
  'Product',
  'Marketing',
  'Sales',
  'HR',
  'Finance',
  'Operations',
] as const;

const JobForm: React.FC = () => {
  const router = useRouter();
  const { id } = router.query;
  const dispatch = useDispatch();
  const jobsState = useSelector((state: RootState) => state.jobs || { jobs: [] });
  const jobs = jobsState.jobs;

  const [formData, setFormData] = useState<Partial<Job>>({
    title: '',
    department: '',
    jobType: '',
    experienceLevel: '',
    location: '',
    description: '',
    requirements: '',
    responsibilities: '',
    salaryRange: '',
    isActive: true,
  });

  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      const job = jobs.find(j => j.id === id);
      if (job) {
        setFormData(job);
      }
    }
  }, [id, jobs]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Basic validation
    if (!formData.title || !formData.department || !formData.jobType || !formData.experienceLevel) {
      setError('Please fill in all required fields.');
      return;
    }
    setError(null);
    if (id) {
      dispatch(updateJob({ ...formData, id } as Job));
    } else {
      const newJob: Job = {
        ...formData,
        id: Date.now().toString(),
        postedDate: new Date().toISOString(),
      } as Job;
      dispatch(addJob(newJob));
    }
    router.push('/jobs');
  };

  return (
    <Box sx={{ p: 3, maxWidth: 1200, margin: '0 auto' }}>
      <Paper sx={{ p: 3 }}>
        {error && (
          <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>
        )}
        <Typography variant="h5" component="h1" gutterBottom>
          {id ? 'Edit Job Posting' : 'Create Job Posting'}
        </Typography>
        <form onSubmit={handleSubmit}>
          <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
            <TextField
              fullWidth
              label="Job Title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              select
              label="Department"
              name="department"
              value={formData.department}
              onChange={handleChange}
              required
            >
              {departments.map((dept) => (
                <MenuItem key={dept} value={dept}>
                  {dept}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              fullWidth
              select
              label="Job Type"
              name="jobType"
              value={formData.jobType}
              onChange={handleChange}
              required
            >
              {jobTypes.map((type) => (
                <MenuItem key={type} value={type}>
                  {type}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              fullWidth
              select
              label="Experience Level"
              name="experienceLevel"
              value={formData.experienceLevel}
              onChange={handleChange}
              required
            >
              {experienceLevels.map((level) => (
                <MenuItem key={level} value={level}>
                  {level}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              fullWidth
              label="Location"
              name="location"
              value={formData.location}
              onChange={handleChange}
              required
            />
            <TextField
              fullWidth
              label="Salary Range"
              name="salaryRange"
              value={formData.salaryRange}
              onChange={handleChange}
              required
            />
            <Box sx={{ gridColumn: '1 / -1' }}>
              <TextField
                fullWidth
                label="Job Description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                required
                multiline
                rows={4}
              />
            </Box>
            <Box sx={{ gridColumn: '1 / -1' }}>
              <TextField
                fullWidth
                label="Requirements"
                name="requirements"
                value={formData.requirements}
                onChange={handleChange}
                required
                multiline
                rows={4}
              />
            </Box>
            <Box sx={{ gridColumn: '1 / -1' }}>
              <TextField
                fullWidth
                label="Responsibilities"
                name="responsibilities"
                value={formData.responsibilities}
                onChange={handleChange}
                required
                multiline
                rows={4}
              />
            </Box>
            <Box sx={{ gridColumn: '1 / -1' }}>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.isActive}
                    onChange={handleChange}
                    name="isActive"
                  />
                }
                label="Active"
              />
            </Box>
            <Box sx={{ gridColumn: '1 / -1', display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
              <Button
                type="button"
                variant="outlined"
                onClick={() => router.push('/jobs')}
              >
                Cancel
              </Button>
              <Button type="submit" variant="contained" color="primary">
                {id ? 'Update' : 'Create'} Job Posting
              </Button>
            </Box>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default JobForm; 