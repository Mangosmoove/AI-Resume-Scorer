import Features from './Features';
import Hero from './Hero';
import { FileText, Upload, X } from "lucide-react";
import { useRef, useState, type DragEvent } from 'react';
import {
  Button,
  TextField,
  // Alert,
  // CircularProgress,
  IconButton,
} from "@mui/material";


const LandingView = () => {
      const [file, setFile] = useState<File | null>(null);
  const [jd, setJd] = useState("");
  // const [status, setStatus] = useState<"idle" | "parsing" | "analyzing">("idle");
  // const [error, setError] = useState<string | null>(null);
  const [dragOver, setDragOver] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);


  const onDrop = (e: DragEvent<HTMLLabelElement>) => {
    e.preventDefault();
    setDragOver(false);
  };

  const onSubmit = async() => {
    console.log('hi');
  };

    return (
        <>
            <Hero />
            <form onSubmit={onSubmit} className="mt-12 grid gap-6 md:grid-cols-2">
              {/* Resume upload */}
              <div className="space-y-2">
                <label className="flex items-center gap-2 text-xs uppercase tracking-widest text-muted-foreground">
                  <span className="text-primary">01.</span> resume.upload
                </label>
                <label
                  onDragOver={(e) => {
                    e.preventDefault();
                    setDragOver(true);
                  }}
                  onDragLeave={() => setDragOver(false)}
                  onDrop={onDrop}
                  className={`group relative flex h-64 cursor-pointer flex-col items-center justify-center gap-3 rounded-md border-2 border-dashed p-6 text-center transition ${
                    dragOver
                      ? "border-primary bg-primary/10 border-glow"
                      : "border-border bg-card/40 hover:border-primary/60 hover:bg-primary/5"
                  }`}
                >
                  <input
                    ref={inputRef}
                    type="file"
                    accept=".pdf,.docx,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    className="hidden"
                    // onChange={(e) => handleFile(e.target.files?.[0] ?? null)}
                  />
                  {file ? (
                    <>
                      <FileText className="h-10 w-10 text-primary text-glow" />
                      <div className="max-w-full truncate text-sm font-semibold text-foreground">
                        {file.name}
                      </div>
                      <div className="text-xs text-muted-foreground">
                        {(file.size / 1024).toFixed(1)} KB · click to change
                      </div>
                      <IconButton
                        size="small"
                        onClick={(e) => {
                          e.preventDefault();
                          e.stopPropagation();
                          setFile(null);
                          if (inputRef.current) inputRef.current.value = "";
                        }}
                        className="!absolute !right-2 !top-2"
                        aria-label="Remove file"
                      >
                        <X className="h-4 w-4" />
                      </IconButton>
                    </>
                  ) : (
                    <>
                      <Upload className="h-10 w-10 text-primary" />
                      <div className="text-sm font-semibold text-foreground">
                        Drop your resume here
                      </div>
                      <div className="text-xs text-muted-foreground">
                        .pdf or .docx · up to 10MB
                      </div>
                    </>
                  )}
                </label>
              </div>

              {/* JD textarea */}
              <div className="space-y-2">
                <label
                  htmlFor="jd"
                  className="flex items-center gap-2 text-xs uppercase tracking-widest text-muted-foreground"
                >
                  <span className="text-primary">02.</span> job_description.paste
                </label>
                <TextField
                  id="jd"
                  value={jd}
                  onChange={(e) => setJd(e.target.value)}
                  placeholder="Paste the job description here..."
                  multiline
                  minRows={9}
                  maxRows={9} //todo: fix height of box to match resume
                  fullWidth
                  helperText={`${jd.length} chars`}
                  slotProps={{
                    formHelperText: {
                      sx: { textAlign: "right", textTransform: "uppercase", fontSize: 10 },
                    },
                  }}
                />
              </div>

              {/* Error + CTA */}
              <div className="md:col-span-2 space-y-4">
                {/* {error && (
                  <Alert severity="error" variant="outlined">
                    {error}
                  </Alert>
                )} */}
                <Button
                  type="submit"
                //   disabled={busy || !file || jd.trim().length < 20}
                  fullWidth
                  size="large"
                  variant="contained"
                  color="primary"
                //   startIcon={
                //     busy ? <CircularProgress size={18} color="inherit" /> : null
                //   }
                  sx={{ py: 2, fontSize: 16, letterSpacing: "0.2em" }}
                >
                  {status === "parsing"
                    ? "> parsing_resume..."
                    : status === "analyzing"
                      ? "> running_ats_scan..."
                      : "> ./beat_the_bot.sh"}
                </Button>
                <p className="text-center text-xs text-muted-foreground">
                  ⚠ Results are AI-generated and may not be fully accurate.
                </p>
              </div>
            </form>

            <Features />
        </>
    )
}

export default LandingView;
