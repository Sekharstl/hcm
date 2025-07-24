import React, { useState } from 'react';
import { Paper, Box, Grid, TextField, Button, IconButton, Snackbar } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import MuiAlert from '@mui/material/Alert';

// Placeholder for API call
async function saveCandidateEducation(educations: any[], candidateId: string) {
  // TODO: Implement actual API call
  return Promise.resolve();
}

interface CandidateEducationFormProps {
  candidateId: string;
  onNext: () => void;
  onBack: () => void;
  education: any[];
  setEducation: (education: any[]) => void;
}

const CandidateEducationForm: React.FC<CandidateEducationFormProps> = ({ candidateId, onNext, onBack, education, setEducation }) => {
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleEducationChange = (idx: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setEducation(education.map((edu, i) => i === idx ? { ...edu, [name]: value } : edu));
  };

  const addEducation = () => {
    setEducation([
      ...education,
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
    setEducation(education.filter((_, i) => i !== idx));
  };

  const handleSaveAndNext = async () => {
    setLoading(true);
    setError(null);
    try {
      await saveCandidateEducation(education, candidateId);
      setSuccess('Education saved successfully!');
      onNext();
    } catch (err) {
      setError('Failed to save education');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ mt: 3 }}>
      {education.map((edu, idx) => (
        <Paper key={idx} sx={{ p: 3, pt: 5, pr: 5, mb: 2, borderRadius: 3, position: 'relative' }}>
          <IconButton aria-label="delete" onClick={() => removeEducation(idx)} sx={{ position: 'absolute', top: 12, right: 12, zIndex: 1 }} disabled={education.length === 1}>
            <DeleteIcon />
          </IconButton>
          <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
            <TextField fullWidth label="Institution Name" name="institutionName" value={edu.institutionName} onChange={e => handleEducationChange(idx, e)} />
            <TextField fullWidth label="Degree" name="degree" value={edu.degree} onChange={e => handleEducationChange(idx, e)} />
            <TextField fullWidth label="Field of Study" name="fieldOfStudy" value={edu.fieldOfStudy} onChange={e => handleEducationChange(idx, e)} />
            <TextField fullWidth label="Start Date" name="startDate" type="date" InputLabelProps={{ shrink: true }} value={edu.startDate} onChange={e => handleEducationChange(idx, e)} />
            <TextField fullWidth label="End Date" name="endDate" type="date" InputLabelProps={{ shrink: true }} value={edu.endDate} onChange={e => handleEducationChange(idx, e)} />
            <TextField fullWidth label="Grade" name="grade" value={edu.grade} onChange={e => handleEducationChange(idx, e)} />
            <TextField fullWidth label="Institution" name="institution" value={edu.institution} onChange={e => handleEducationChange(idx, e)} />
            <TextField fullWidth label="Notes" name="notes" value={edu.notes} onChange={e => handleEducationChange(idx, e)} multiline rows={2} />
            <TextField fullWidth label="Description" name="description" value={edu.description} onChange={e => handleEducationChange(idx, e)} multiline rows={2} />
          </Box>
          {idx === education.length - 1 && (
            <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
              <Button variant="outlined" onClick={onBack}>Back</Button>
              <Button variant="outlined" onClick={addEducation}>Add Education</Button>
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

export default CandidateEducationForm; 