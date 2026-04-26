import { useEffect, useState } from "react";
import "./App.css";

import { getLayoutByUserType } from "./api/uiApi";
import { AnalyticsPreview } from "./components/AnalyticsPreview";
import { AssistantWidget } from "./components/AssistantWidget";
import { BankingDemo } from "./components/BankingDemo";
import { LandingHero } from "./components/LandingHero";
import { LayoutPreview } from "./components/LayoutPreview";
import { LayoutSwitcher } from "./components/LayoutSwitcher";
import { WalkthroughOverlay } from "./components/WalkthroughOverlay";
import { onboardingWalkthrough } from "./mocks/walkthroughMock";
import type { BankPage, WalkthroughStep } from "./types/assistant";
import type { AdaptiveLayout, UserType } from "./types/ui";

function App() {
    const [selectedType, setSelectedType] = useState<UserType>("DEFAULT");
    const [layout, setLayout] = useState<AdaptiveLayout | null>(null);
    const [loadingLayout, setLoadingLayout] = useState(false);

    const [bankPage, setBankPage] = useState<BankPage>("overview");

    const [steps, setSteps] = useState<WalkthroughStep[]>([]);
    const [activeStep, setActiveStep] = useState(0);

    useEffect(() => {
        async function loadLayout() {
            try {
                setLoadingLayout(true);
                const data = await getLayoutByUserType(selectedType);
                setLayout(data);
            } finally {
                setLoadingLayout(false);
            }
        }

        loadLayout();
    }, [selectedType]);

    function startWalkthrough(newSteps: WalkthroughStep[]) {
        const firstPage = newSteps[0]?.page;

        if (firstPage) {
            setBankPage(firstPage);
        }

        setSteps(newSteps);
        setActiveStep(0);
    }

    function closeWalkthrough() {
        setSteps([]);
        setActiveStep(0);
    }

    function nextStep() {
        if (activeStep === steps.length - 1) {
            closeWalkthrough();
            return;
        }

        const nextIndex = activeStep + 1;
        const nextPage = steps[nextIndex]?.page;

        if (nextPage) {
            setBankPage(nextPage);
        }

        setActiveStep(nextIndex);
    }

    function prevStep() {
        const prevIndex = Math.max(0, activeStep - 1);
        const prevPage = steps[prevIndex]?.page;

        if (prevPage) {
            setBankPage(prevPage);
        }

        setActiveStep(prevIndex);
    }

    return (
        <div className="app-shell">
            <LandingHero
                onStartOnboarding={() => startWalkthrough(onboardingWalkthrough)}
            />

            <LayoutSwitcher selectedType={selectedType} onChange={setSelectedType} />

            <LayoutPreview layout={layout} loading={loadingLayout} />

            <BankingDemo
                layout={layout}
                page={bankPage}
                onChangePage={setBankPage}
            />

            <AnalyticsPreview />

            <AssistantWidget
                onStartWalkthrough={startWalkthrough}
                activeStep={steps[activeStep] ?? null}
            />

            <WalkthroughOverlay
                steps={steps}
                activeIndex={activeStep}
                onNext={nextStep}
                onPrev={prevStep}
                onClose={closeWalkthrough}
            />
        </div>
    );
}

export default App;