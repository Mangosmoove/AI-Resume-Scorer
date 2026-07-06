import Header from './components/Header';
import Footer from './components/Footer';
import LandingView from './components/LandingView';


function App() {
  // const [result, setResult] = useState<AnalysisResult | null>(null);

  // const resubmit = () => {
  //   setResult(null);
  //   // keep JD and file so user can iterate
  //   window.scrollTo({ top: 0, behavior: "smooth" });
  // };
  
 return (
    <div className="min-h-screen">
      <Header />
      <main className="mx-auto max-w-6xl px-4 py-10 md:py-16">
        {/* {!result ? ( */}
          <LandingView />
        {/* ) : ( */}
          {/* // <ResultsView result={result} onResubmit={resubmit} /> */}
          {/* <></> */}
        {/* )} */}
      </main>
      <Footer />
    </div>
  );
}

export default App
