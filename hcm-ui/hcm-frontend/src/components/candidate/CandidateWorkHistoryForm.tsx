import React, { useState } from 'react';
import { Paper, Box, Grid, TextField, Button, IconButton, Snackbar } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import MuiAlert from '@mui/material/Alert';

// Placeholder for API call
async function saveCandidateWorkHistory(workHistory: any[], candidateId: string) {
  // TODO: Implement actual API call
  return Promise.resolve();
}

interface CandidateWorkHistoryFormProps {
  candidateId: string;
  onNext: () => void;
  onBack: () => void;
  workHistory: any[];
  setWorkHistory: (workHistory: any[]) => void;
}

const CandidateWorkHistoryForm: React.FC<CandidateWorkHistoryFormProps> = ({ candidateId, onNext, onBack, workHistory, setWorkHistory }) => {
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleWorkHistoryChange = (idx: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setWorkHistory(workHistory.map((wh, i) => i === idx ? { ...wh, [name]: value } : wh));
  };

  const addWorkHistory = () => {
    setWorkHistory([
      ...workHistory,
      {
        workHistoryId: undefined,
        companyName: '',
        positionTitle: '',
        location: '',
        startDate: '',
        endDate: '',
        responsibilities: '',
        description: '',
        jobTitle: '',
      },
    ]);
  };

  const removeWorkHistory = (idx: number) => {
    setWorkHistory(workHistory.filter((_, i) => i !== idx));
  };

  const handleSaveAndNext = async () => {
    setLoading(true);
    setError(null);
    try {
      await saveCandidateWorkHistory(workHistory, candidateId);
      setSuccess('Work history saved successfully!');
      onNext();
    } catch (err) {
      setError('Failed to save work history');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ mt: 3 }}>
      {workHistory.map((wh, idx) => (
        <Paper key={idx} sx={{ p: 3, pt: 5, pr: 5, mb: 2, borderRadius: 3, position: 'relative' }}>
          <IconButton aria-label="delete" onClick={() => removeWorkHistory(idx)} sx={{ position: 'absolute', top: 12, right: 12, zIndex: 1 }} disabled={workHistory.length === 1}>
            <DeleteIcon />
          </IconButton>
          <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
            <TextField fullWidth label="Company Name" name="companyName" value={wh.companyName} onChange={e => handleWorkHistoryChange(idx, e)} />
            <TextField fullWidth label="Position Title" name="positionTitle" value={wh.positionTitle} onChange={e => handleWorkHistoryChange(idx, e)} />
            <TextField fullWidth label="Job Title" name="jobTitle" value={wh.jobTitle} onChange={e => handleWorkHistoryChange(idx, e)} />
            <TextField fullWidth label="Location" name="location" value={wh.location} onChange={e => handleWorkHistoryChange(idx, e)} />
            <TextField fullWidth label="Start Date" name="startDate" type="date" InputLabelProps={{ shrink: true }} value={wh.startDate} onChange={e => handleWorkHistoryChange(idx, e)} />
            <TextField fullWidth label="End Date" name="endDate" type="date" InputLabelProps={{ shrink: true }} value={wh.endDate} onChange={e => handleWorkHistoryChange(idx, e)} />
            <TextField fullWidth label="Responsibilities" name="responsibilities" value={wh.responsibilities} onChange={e => handleWorkHistoryChange(idx, e)} multiline rows={2} />
            <TextField fullWidth label="Description" name="description" value={wh.description} onChange={e => handleWorkHistoryChange(idx, e)} multiline rows={2} />
          </Box>
          {idx === workHistory.length - 1 && (
            <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
              <Button variant="outlined" onClick={onBack}>Back</Button>
              <Button variant="outlined" onClick={addWorkHistory}>Add Work History</Button>
              <Button variant="contained" color="primary" onClick={handleSaveAndNext} disabled={loading}>Save & Next</Button>
            </Box>
          )}
        </Paper>
      ))}
      <Snackbar open={!!success} autoHideDuration={1200} onClose={() => setSuccess(null)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <MuiAlert onClose={() => setSuccess(null)} severity="success" sx={{ width: '100%' }} elevation={6} variant="filled">{success}</MuiAlert>
      </Snackbar>
      <Snackbar open={!!error} autoHideDuration={2000} onClose={() => setError(null)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <MuiAlert onClose={() => setError(null)} severity="error" sx={{ width: '100%' }} elevation={6} variant="filled">{error}</MuiAlert>
      </Snackbar>
    </Box>
  );
};

export default CandidateWorkHistoryForm; 