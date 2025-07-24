import React, { useState, useEffect } from 'react';
import { Box, Stepper, Step, StepLabel, Paper, Button, TextField, MenuItem, Snackbar, CircularProgress } from '@mui/material';
import MuiAlert from '@mui/material/Alert';
import CandidateEducationForm from './CandidateEducationForm';
import CandidateSkillForm from './CandidateSkillForm';
import CandidateWorkHistoryForm from './CandidateWorkHistoryForm';
import CandidateCertificationForm from './CandidateCertificationForm';
import { fetchCandidateById } from '../../services/candidateService';
import { useRouter } from 'next/router';

const genders = ['Male', 'Female', 'Other'];

// Basic Details Form as a step
const BasicDetailsForm = ({ formData, onChange, onSubmit, loading, error, success, setSuccess, setError }: any) => {
  const router = useRouter();
  return (
    <Paper sx={{ p: 3, mt: 3 }}>
      <form onSubmit={onSubmit}>
        <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 3 }}>
          <TextField
            fullWidth
            label="First Name"
            name="firstName"
            value={formData.firstName}
            onChange={onChange}
            required
          />
          <TextField
            fullWidth
            label="Middle Name"
            name="middleName"
            value={formData.middleName}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Last Name"
            name="lastName"
            value={formData.lastName}
            onChange={onChange}
            required
          />
          <TextField
            fullWidth
            label="Email"
            name="email"
            type="email"
            value={formData.email}
            onChange={onChange}
            required
          />
          <TextField
            fullWidth
            label="Phone"
            name="phone"
            value={formData.phone}
            onChange={onChange}
            required
          />
          <TextField
            fullWidth
            label="Address"
            name="address"
            value={formData.address}
            onChange={onChange}
            required
          />
          <TextField
            fullWidth
            label="City"
            name="city"
            value={formData.city}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="State"
            name="state"
            value={formData.state}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Country"
            name="country"
            value={formData.country}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Postal Code"
            name="postalCode"
            value={formData.postalCode}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Date of Birth"
            name="dateOfBirth"
            type="date"
            value={formData.dateOfBirth}
            onChange={onChange}
            InputLabelProps={{ shrink: true }}
            required
          />
          <TextField
            fullWidth
            select
            label="Gender"
            name="gender"
            value={formData.gender}
            onChange={onChange}
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
            onChange={onChange}
            required
          />
          <TextField
            fullWidth
            label="LinkedIn URL"
            name="linkedinUrl"
            value={formData.linkedinUrl}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Current Salary"
            name="currentSalary"
            type="number"
            value={formData.currentSalary}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Expected Salary"
            name="expectedSalary"
            type="number"
            value={formData.expectedSalary}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Notice Period (days)"
            name="noticePeriod"
            type="number"
            value={formData.noticePeriod}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Availability Date"
            name="availabilityDate"
            type="date"
            value={formData.availabilityDate}
            onChange={onChange}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            fullWidth
            label="Status"
            name="status"
            value={formData.status}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Source"
            name="source"
            value={formData.source}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Vendor ID"
            name="vendorId"
            value={formData.vendorId}
            onChange={onChange}
          />
          <TextField
            fullWidth
            label="Notes"
            name="notes"
            value={formData.notes}
            onChange={onChange}
            multiline
            rows={2}
          />
        </Box>
        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end', mt: 3 }}>
          <Button
            type="button"
            variant="outlined"
            color="secondary"
            onClick={() => router.push('/dashboard/candidates')}
          >
            Cancel
          </Button>
          <Button type="submit" variant="contained" color="primary" disabled={loading}>
            {loading ? 'Saving...' : 'Save & Next'}
          </Button>
        </Box>
      </form>
      <Snackbar open={!!success} autoHideDuration={1200} onClose={() => setSuccess(null)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <MuiAlert onClose={() => setSuccess(null)} severity="success" sx={{ width: '100%' }} elevation={6} variant="filled">
          {success}
        </MuiAlert>
      </Snackbar>
      <Snackbar open={!!error} autoHideDuration={2000} onClose={() => setError(null)} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <MuiAlert onClose={() => setError(null)} severity="error" sx={{ width: '100%' }} elevation={6} variant="filled">
          {error}
        </MuiAlert>
      </Snackbar>
    </Paper>
  );
};

const steps = [
  'Basic Details',
  'Education',
  'Skills',
  'Work History',
  'Certifications',
];

const initialFormData = {
  candidateId: '',
  tenantId: '',
  organizationId: '',
  firstName: '',
  middleName: '',
  lastName: '',
  email: '',
  phone: '',
  address: '',
  city: '',
  state: '',
  country: '',
  postalCode: '',
  dateOfBirth: '',
  gender: '',
  nationality: '',
  linkedinUrl: '',
  currentSalary: '',
  expectedSalary: '',
  noticePeriod: '',
  availabilityDate: '',
  status: '',
  source: '',
  vendorId: '',
  notes: '',
};

interface CandidateStepperFormProps {
  candidateId?: string;
}

const CandidateStepperForm: React.FC<CandidateStepperFormProps> = ({ candidateId: propCandidateId }) => {
  const [activeStep, setActiveStep] = useState(0);
  const [formData, setFormData] = useState(initialFormData);
  const [education, setEducation] = useState([
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
  const [skills, setSkills] = useState([
    {
      skillId: undefined,
      proficiencyLevel: '',
      yearsOfExperience: '',
    },
  ]);
  const [workHistory, setWorkHistory] = useState([
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
  const [certifications, setCertifications] = useState([
    {
      certificationId: undefined,
      certificateName: '',
      issuedBy: '',
      issueDate: '',
      expiryDate: '',
      certificationName: '',
      issuingOrganization: '',
    },
  ]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [candidateId, setCandidateId] = useState(propCandidateId || '');
  const [fetching, setFetching] = useState(false);
  const router = useRouter();
  const [showSuccess, setShowSuccess] = useState(false);

  // Fetch candidate data for edit mode
  useEffect(() => {
    if (propCandidateId) {
      setFetching(true);
      fetchCandidateById(propCandidateId)
        .then(data => {
          setFormData({
            ...initialFormData,
            ...data,
          });
          setCandidateId(propCandidateId);
        })
        .catch(() => setError('Failed to fetch candidate details'))
        .finally(() => setFetching(false));
    }
  }, [propCandidateId]);

  // Handlers for basic details
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleBasicDetailsSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      // Simulate API call to create/update candidate and get candidateId
      // Replace with your actual API logic
      let newCandidateId = candidateId;
      if (!candidateId) {
        newCandidateId = 'generated-candidate-id'; // Replace with real ID from API
        setCandidateId(newCandidateId);
      }
      setSuccess('Candidate details saved!');
      setActiveStep(1);
    } catch (err) {
      setError('Failed to save candidate details');
    } finally {
      setLoading(false);
    }
  };

  // Stepper navigation
  const handleNext = () => setActiveStep((prev) => prev + 1);
  const handleBack = () => setActiveStep((prev) => prev - 1);
  const handleStep = (step: number) => setActiveStep(step);

  // Success handler for final step
  const handleFinalStep = () => {
    setShowSuccess(true);
    setTimeout(() => {
      router.push('/dashboard/candidates');
    }, 1500);
  };

  if (fetching) {
    return (
      <Box sx={{ maxWidth: 800, margin: '0 auto', p: 3, display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 300 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ maxWidth: 800, margin: '0 auto', p: 3 }}>
      <Stepper activeStep={activeStep} alternativeLabel>
        {steps.map((label, index) => (
          <Step key={label}>
            <StepLabel onClick={() => index <= activeStep ? handleStep(index) : undefined} style={{ cursor: index <= activeStep ? 'pointer' : 'default' }}>
              {label}
            </StepLabel>
          </Step>
        ))}
      </Stepper>
      {activeStep === 0 && (
        <BasicDetailsForm
          formData={formData}
          onChange={handleChange}
          onSubmit={handleBasicDetailsSubmit}
          loading={loading}
          error={error}
          success={success}
          setSuccess={setSuccess}
          setError={setError}
        />
      )}
      {activeStep === 1 && candidateId && (
        <CandidateEducationForm
          candidateId={candidateId}
          onNext={handleNext}
          onBack={handleBack}
          education={education}
          setEducation={setEducation}
        />
      )}
      {activeStep === 2 && candidateId && (
        <CandidateSkillForm
          candidateId={candidateId}
          onNext={handleNext}
          onBack={handleBack}
          skills={skills}
          setSkills={setSkills}
        />
      )}
      {activeStep === 3 && candidateId && (
        <CandidateWorkHistoryForm
          candidateId={candidateId}
          onNext={handleNext}
          onBack={handleBack}
          workHistory={workHistory}
          setWorkHistory={setWorkHistory}
        />
      )}
      {activeStep === 4 && candidateId && (
        <CandidateCertificationForm
          candidateId={candidateId}
          onNext={handleFinalStep}
          onBack={handleBack}
          certifications={certifications}
          setCertifications={setCertifications}
        />
      )}
      {showSuccess && (
        <Snackbar open={showSuccess} autoHideDuration={1500} anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
          <MuiAlert severity="success" sx={{ width: '100%' }} elevation={6} variant="filled">
            Candidate profile completed successfully!
          </MuiAlert>
        </Snackbar>
      )}
    </Box>
  );
};

export default CandidateStepperForm; 